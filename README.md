# mapleDocs
System for managing machine readable data management plans (maDMPs) created for Data Stewardship course at TU Vienna

With mapleDocs users can upload their maDMPs, they are then stored in a MongoDB and can be searched very efficiently with
the help of Elasticsearch. Furthermore the application is fully containerized with Docker.

 ## Building and running
 
 ### Running the backend
 
First, make sure max_map_count is at least 262144, otherwise set it with: ´sudo sysctl -w vm.max_map_count=262144´. If it is
less, the elasticsearch service might fail.
 
 * Build the spring application: run ´mvn -Dmaven.test.skip=true package´ in backend/
 
 * Build the docker container: run run ´docker build -t mapledocs-app:latest .´
 
 * Run the services with docker-compose: ´docker-compose up´

Alternatively, just use the script ´build_and_run.sh´

### Running the frontend

In frontend/, run `npm install` and `npm start` to start the development server