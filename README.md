# Introduction
This project was created for the course Data Stewardship, summer term 2020 at TU Vienna.
The goal of this open source project is to provide a system that allows users to manage and expose machine readable data management plans (maDMPs) to achieve FAIR data (see [TU Vienna FAIR](https://www.tuwien.at/forschung/fti-support/forschungsdaten/forschungsdatenmanagement/fair-prinzipien/), [Exposing maDMPs](https://www.rd-alliance.org/groups/exposing-data-management-plans-wg)).

## Features
- Upload maDMPs as JSON files (stored in MongoDB for scalability and flexibility)
- Download and save maDMPs
- Hide specified fields of maDMPs such that they become invisible to other users
- Hide nested fields with complex JSONpath expressions
- Schema validation against the maDMP schema provided by the [RDA-DMP common standard](https://github.com/RDA-DMP-Common/RDA-DMP-Common-Standard)
- Optional, automated asignment of DOIs to maDMPs from [Datacite](https://datacite.org/) if no DOI is present on upload
- Interactive maDMP viewer for a clear view over maDMPs 
- Fast, powerful search of maDMPs:
    - field-wise search on maDMPs
    - combined search over multiple maDMP fields at once
    - full-text search over the full maDMP
- Import maDMPs from a Zenodo community

## Technical Implementation
- Security: Registration and Login with JWT. Secured with Spring Security
- Easy to use and simple UI written in Angular
- Extendable and easy to maintain layered architecture
- Built as Spring Boot application, written in Java
- All components (MongoDB, ElasticSearch and the Spring Boot application) are deployed with Docker for a highly portable, scalable and easy way of deployment. To that end we provide a docker-compose containing the backend components. The fronend can be deployed as Angular application on a webserver
- Search implemented with Elasticsearch for high flexibility, extendability and scalability

## Key technologies 
### Backend:
- [MongoDB](https://www.mongodb.com/) for efficient data access and flexible storage
- [ElasticSearch](https://www.elastic.co/de/) for full-text and highly efficient and scalable searching
- [Spring Boot](https://spring.io/projects/spring-boot) for a secure, scalable and highly maintainable backend application
- [Docker](https://www.docker.com/) for containerization for easy and portable deployment 

### Front end:
- [Angular](https://angular.io/) and material design for a modern and flexible UX

# Installation and Running the project 
## Prerequisites
- Java 11
- Maven
- Docker
- npm and node.js
- Tested in Chrome only

## Backend (Application Server)
First, make sure max_map_count is at least 262144, otherwise set its value with the command `sudo sysctl -w vm.max_map_count=262144`. If the value is too low, the elasticsearch service might fail.
 
 * Build the spring application (in backend/): `mvn -Dmaven.test.skip=true package`
 
 * Build the docker container: `docker build -t mapledocs-app:latest .` (might need to run as root)
 
 * Run the services with docker-compose: `docker-compose up` (might need to run as root)

Alternatively, just use the script `build_and_start.sh`

## Frontend

In frontend/, run `npm install` and `npm start` to start the development server

## Usage
After following the steps above for both backend and frontend, the application is reachable on localhost port 4200. The standard test user created is: 
- username: admin
- password: password

In Order to be able to assign new dois, the Datacite account has to be configured. In the resources folder of the backend project (on the same level as application.properties) create  a file with the name "auth.properties" and the following content:

```
application.rest.doiservice.username=[username]
application.rest.doiservice.password=[password]
application.rest.doiservice.prefix=[doi prefix]
```

## Importing maDMPs from the Zenodo community

The DMPs from the [Data Stewardship Community](https://zenodo.org/communities/tuw-dmps-ds-2020) can be downloaded using a simple [python script](https://github.com/lucasberent/mapleDocs/blob/master/backend/import_data.py), which fetches all DMPs using the Zenodo API, authenticates with mapleDocs and automatically uploads all of them. Of course this script can easily be configured to fetch maDMPs from another api and upload them to mapleDocs.

# Local Development
Some tips for local development and setup: 
- If you start the application in an IDE, adapt the links in the Constants and properties files to point to localhost instead of the docker container names


# Examples and Screencasts
Follow the links below to get a short preview of the look and some first features of the application:

## Uploading maDMPs
In this screencast we present the submission (upload) of maDMPs to the mapleDocs server, including the automated assignment of a new DOI if the maDMP to upload does not have one yet.

[screencast](https://youtu.be/V8iSmcFxd88)

## Search
Here we present another core feature: mapleDocs powerful search, encompassing the three search modes: full text search over full maDMPs, combined search and custom search.

[screencast](https://youtu.be/PWKb_2rSIx8)

## Hiding fields
In this screencast we present hiding of fields for other users with json path expressions on maDMP upload.

[screencast](https://youtu.be/YT1ndTUG7TU)

## Architectural Diagram
![Architectural Diagram](https://raw.githubusercontent.com/lucasberent/mapleDocs/master/architecture-diagram.png)

# Resources
## Schema and maDMPs
- [RDA-DMP Common Standard](https://github.com/RDA-DMP-Common/RDA-DMP-Common-Standard)
- The schema is saved in the resources folder of the backend project. This is used by the validation resource.

# Contributors
- [Alexander Selzer](https://github.com/arselzer)
- [Lucas Berent](https://github.com/lucasberent)

# License
- [MIT](https://github.com/lucasberent/mapleDocs/blob/master/LICENSE)
