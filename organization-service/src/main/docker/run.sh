#!/bin/sh
echo "********************************************************"
echo "Starting Organization Service  "
echo "********************************************************"

dockerize -wait http://eurekaserver:8761 -wait http://configserver:8888 -wait http://database:5432 -wait http://authenticationservice:8901

java -Djava.security.egd=file:/dev/./urandom -Dserver.port=$SERVER_PORT   \
     -Deureka.client.serviceUrl.defaultZone=$EUREKASERVER_URI             \
     -Dspring.cloud.config.uri=$CONFIGSERVER_URI                          \
     -Dspring.profiles.active=$PROFILE                                    \
     -jar /usr/local/organizationservice/@project.build.finalName@.jar
