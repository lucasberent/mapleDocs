# Introduction
This project was made in the course of the course Data Stewardship, summer term 2020 at TU Vienna. The goal of this opensource project is to provide a system that allows users to manage and expose machine readible data management plans to achieve FAIR data [TU Vienna FAIR](https://www.tuwien.at/forschung/fti-support/forschungsdaten/forschungsdatenmanagement/fair-prinzipien/), [Exposing maDMPs](https://www.rd-alliance.org/groups/exposing-data-management-plans-wg).

## Key features
We have built a highly scalable and modern system that allows users to:
- Upload maDMPs which are validated according to the standard defined in [RDA DMP Common Standard](https://github.com/RDA-DMP-Common/RDA-DMP-Common-Standard)
- Control which fields are visible to other users of the system i.e. specifically hide fields of the maDMP
- View and Download maDMPs
- Search for maDMPs

## Key technologies 
### Backend:
- [MongoDB](https://www.mongodb.com/) for efficient data access and flexible storage
- [ElasticSearch](https://www.elastic.co/de/) and [Logstash](https://www.elastic.co/de/logstash) for full-text and highly efficient and scalable searching
- [Spring Boot](https://spring.io/projects/spring-boot) for a secures,scalable and highly maintainable application
- [Docker](https://www.docker.com/) for containerization for easy and portable deployment 

### Front end:
- [Anuglar](https://angular.io/) and material design for a modern and flexible UX

# Prerequisites
- In order to assign dois to maDMPs at upload, we have integrated the DataCite API, hence if one wants to use this feature a Datacite account is necessary. In order to configure the account refer to Chapter *Configuration*.

# Configuration
The application assigns new dois to maDMPs that do not have one yet at upload time. In order to do that we have integrated the datacite API. The respective configuration has to be done in a .properties file: "auth.properties" in the same directory as the application.properties file. The needed properties are: 
- application.rest.doiservice.username
- application.rest.doiservice.password
- application.rest.doiservice.prefix

Once an according file is created and the properties are set accordingly, the application can start and assign new dois to maDMPs on upload.

# Installation and Running the project 
- Backend: refer to the README in the [/backend directory](https://github.com/lucasberent/mapleDocs/blob/master/backend/README.md)
- Frontend: refer to the README in the [/frontend directory](https://github.com/lucasberent/mapleDocs/blob/master/frontend/mapdledocsapp/README.md)

# Examples and Screencasts

## Login
## Upload
## Search
## View & Download

# Resources
## Schema and maDMPs
- https://github.com/RDA-DMP-Common/RDA-DMP-Common-Standard

# Contributors
- [Alexander Selzer](https://github.com/arselzer)
- [Lucas Berent](https://github.com/lucasberent)

# License
- https://choosealicense.com/licenses/mit/
- [MIT](https://github.com/lucasberent/mapleDocs/blob/master/backend/LICENSE)
