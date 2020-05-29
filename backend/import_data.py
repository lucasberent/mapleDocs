#!/usr/bin/env python
import urllib.request
from urllib.error import HTTPError
import libxml2
import json
import zipfile
import os

zenodo_base = "https://zenodo.org/api"
server_base = "http://localhost:8080/api/v1"
dmp_list_url = "https://zenodo.org/oai2d?verb=ListRecords&set=user-tuw-dmps-ds-2020&metadataPrefix=oai_dc"

auth_body = {
    "login": "admin",
    "password": "password"
}

auth_req = urllib.request.Request(server_base + "/authentication")
auth_req.add_header('Content-Type', 'application/json; charset=utf-8')
json_body = json.dumps(auth_body).encode("utf-8")
auth_req.add_header('Content-Length', len(json_body))
auth_response = urllib.request.urlopen(auth_req, json_body)
token = json.loads(auth_response.read())["payload"]

print("authenticated: token: " + token)

dmp_list_response = urllib.request.urlopen(dmp_list_url)

dmp_list = dmp_list_response.read().decode("utf-8")
dmp_list_doc = libxml2.parseDoc(dmp_list)

ctx = dmp_list_doc.xpathNewContext()
ctx.xpathRegisterNs("oai", "http://www.openarchives.org/OAI/2.0/")
result = ctx.xpathEval("//oai:ListRecords/oai:record/oai:header/oai:identifier")

ids = list(map(lambda node: node.content.split(":")[2], result))

zips = []

# for dmp_id in ids:
#     response = urllib.request.urlopen(zenodo_base + "/records/" + dmp_id)
#     if response.getcode() == 200:
#         data = json.loads(response.read())
#         print("fetched record: " + dmp_id)
#         files = data["files"]
#         for file in files:
#             if file["type"] == "zip":
#                 zip_url = file["links"]["self"]
#                 if (zip_url != None):
#                     zips.append(zip_url)
#                     print("found zip: " + zip_url)
#     else:
#         print("error: returned " + str(response.getcode()))
#
# if not os.path.exists("zip_files"):
#     os.mkdir("zip_files")
# counter = 0
# for zip_url in zips:
#     response = urllib.request.urlopen(zip_url)
#     file = open("zip_files/" + str(counter) + ".zip", mode="wb")
#     file.write(response.read())
#     counter += 1
#
# if not os.path.exists("dmp_files"):
#     os.mkdir("dmp_files")
for zip_file in os.listdir("zip_files"):
    with zipfile.ZipFile("zip_files/" + zip_file, "r") as zip:
        for contained_file_name in zip.namelist():
            print("zip contains file: " + contained_file_name)

            contained_file = zip.open(contained_file_name)

            if (contained_file_name.lower().endswith(".json")):
                file_contents = contained_file.read()

                try:
                    dmp_body = {
                        "json": file_contents.decode("utf-8").replace("\n", ""),
                        "userId": 0,
                        "docId": None,
                        "fieldToHide": []
                    }
                    json_body = json.dumps(dmp_body).encode("utf-8")
                    print(json_body)
                    dmp_req = urllib.request.Request(server_base + "/madmps")
                    dmp_req.add_header('Content-Type', 'application/json; charset=utf-8')
                    dmp_req.add_header('Authorization', 'Bearer ' + token)
                    dmp_req.add_header('Content-Length', len(json_body))
                    try:
                        dmp_response = urllib.request.urlopen(dmp_req, json_body)

                        if (dmp_response.getcode() == 201):
                            print("upload successful")
                        else:
                            print("error uploading DMP: " + dmp_response.getcode())
                    except HTTPError as e:
                        print("error: " + str(e))
                except Exception as e:
                    print("error " + str(e))

