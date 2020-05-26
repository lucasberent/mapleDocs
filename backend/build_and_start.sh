#!/bin/bash

mvn -Dmaven.test.skip=true package
sudo docker build -t mapledocs-app:latest .
sudo docker-compose up