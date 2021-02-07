#!/bin/sh
echo "********************************************************"
echo "Starting Authentication Service                           "
echo "********************************************************"

sleep 60

#dockerize -wait http://eurekaserver:8761 -wait http://configserver:8888 -wait http://database:5432
dockerize -wait $EUREKASERVER_URI -wait $CONFIGSERVER_URI -wait $DATABASESERVER_PORT

java -Djava.security.egd=file:/dev/./urandom -Dserver.port=$SERVER_PORT   \
     -Deureka.client.serviceUrl.defaultZone=$EUREKASERVER_URI             \
     -Dspring.cloud.config.uri=$CONFIGSERVER_URI                          \
     -Dspring.profiles.active=$PROFILE                                   \
     -jar /usr/local/authenticationservice/@project.build.finalName@.jar
