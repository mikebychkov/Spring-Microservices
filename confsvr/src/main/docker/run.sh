#!/bin/sh
echo "********************************************************"
echo "Starting Configuration Server"
echo "********************************************************"

#dockerize -wait http://eurekaserver:8761
dockerize -wait $EUREKASERVER_URI

java -Djava.security.egd=file:/dev/./urandom -Deureka.client.serviceUrl.defaultZone=$EUREKASERVER_URI -jar /usr/local/configserver/@project.build.finalName@.jar
