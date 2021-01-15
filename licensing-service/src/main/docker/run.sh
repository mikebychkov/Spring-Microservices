#!/bin/sh
echo "********************************************************"
echo "Starting Licensing Service"
echo "********************************************************"

dockerize -wait http://configserver:8888 -wait http://database:5432

java -Dspring.cloud.config.uri=$CONFIGSERVER_URI -Dspring.profiles.active=$PROFILE -jar /usr/local/licensingservice/@project.build.finalName@.jar
