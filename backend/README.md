# User Guide
## Local Setup
The full application and its dependencies, i.e. MongoDB and Postgres databases and the elasticsearch server are deployed
in a docker-compose. Simply run docker-compose-up to start the full application and all dependencies. 
 
 ## Building and running
 
First, make sure max_map_count is at least 262144, otherwise set it with: ´sudo sysctl -w vm.max_map_count=262144´. If it is
less, the elasticsearch service might fail.
 
 * Build the spring application: run ´mvn -Dmaven.test.skip=true package´ in backend/
 
 * Build the docker container: run run ´docker build -t mapledocs-app:latest .´
 
 * Run the services with docker-compose: ´docker-compose up´
