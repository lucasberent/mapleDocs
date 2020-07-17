# Introduction
This project was created for the course Data Stewardship, summer term 2020 at TU Vienna. The goal of this opensource project is to provide a system that allows users to manage and expose machine readible data management plans (maDMPs) to achieve FAIR data [TU Vienna FAIR](https://www.tuwien.at/forschung/fti-support/forschungsdaten/forschungsdatenmanagement/fair-prinzipien/), [Exposing maDMPs](https://www.rd-alliance.org/groups/exposing-data-management-plans-wg).

## Feautres
- Upload maDMPs as Json files (stored in MongoDB for scalability and flexibility)
- Hide fields of maDMPs s.t. they become invisible to other users
- Schema validation against the maDMP schema provided by the [RDA-DMP common standard](https://github.com/RDA-DMP-Common/RDA-DMP-Common-Standard) in frontend and backend on upload
- Optional, automatized assignment of dois to maDMPs from [Datacite](https://datacite.org/) if no doi is present on upload
- Interactive maDMP viewer for a clear view over maDMPs 
- Fast, powerful and extendable search of maDMPs with Elasticsearch:
    - field-wise search on maDMPs
    - combined search over multiple maDMP fields at once
- Download and save maDMPs as Json files
- Security: Registration and Login with JWT. Secured with Spring Security
- Easy to use and simple UI written in Angular
- Extendable and easy to maintain layered architecture
- Built as Spring Boot application, written in Java
- All components (MongoDB, Elasticsearch and the Spring Boot application) are deployed with Docker for a highly portable, scalable and easy way of deployment. To that end we provide a docker-compose containing the backend components. The fronend can be deployed as Angular application on a webserver
- Moreover, we provide an automatized import mechanism to import maDMPs from an external provider e.g. from zenodo, written in Python.

## Key technologies 
### Backend:
- [MongoDB](https://www.mongodb.com/) for efficient data access and flexible storage
- [ElasticSearch](https://www.elastic.co/de/) and [Logstash](https://www.elastic.co/de/logstash) for full-text and highly efficient and scalable searching
- [Spring Boot](https://spring.io/projects/spring-boot) for a secure, scalable and highly maintainable backend application
- [Docker](https://www.docker.com/) for containerization for easy and portable deployment 

### Front end:
- [Angular](https://angular.io/) and material design for a modern and flexible UX

# Installation and Running the project 
## Prerequisites
- Docker
- Tested in Chrome only

## Backend (Application Server)
First, make sure max_map_count is at least 262144, otherwise set it with: `sudo sysctl -w vm.max_map_count=262144`. If it is
less, the elasticsearch service might fail.
 
 * Build the spring application (in backend/): `mvn -Dmaven.test.skip=true package`
 
 * Build the docker container: `docker build -t mapledocs-app:latest .` (might need to run as root)
 
 * Run the services with docker-compose: `docker-compose up` (might need to run as root)

Alternatively, just use the script `build_and_run.sh`

## Frontend

In frontend/, run `npm install` and `npm start` to start the development server

## Importing maDMPs from the Zenodo community

The DMPs from the [Data Stewardship Community](https://zenodo.org/communities/tuw-dmps-ds-2020) can be downloaded using a simple [python script](https://github.com/lucasberent/mapleDocs/blob/master/backend/import_data.py), which fetches all DMPs using the Zenodo API, authenticates with mapleDocs and automatically uploads all of them. Of course this script can easily be configured to fetch maDMPs from another api and upload them to mapleDocs.

# Examples and Screencasts
Follow the links below to get a short preview of the look and some first features of the application:
## Login & Overview
[screencast](https://youtu.be/LkV8qi128ws)
## Upload, Download & Search
[screencast](https://youtu.be/SuS9FClZrCI)

# Resources
## Schema and maDMPs
- https://github.com/RDA-DMP-Common/RDA-DMP-Common-Standard

# Contributors
- [Alexander Selzer](https://github.com/arselzer)
- [Lucas Berent](https://github.com/lucasberent)

# License
- https://choosealicense.com/licenses/mit/
- [MIT](https://github.com/lucasberent/mapleDocs/blob/master/LICENSE)
