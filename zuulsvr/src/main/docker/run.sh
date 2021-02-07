#!/bin/sh
echo "********************************************************"
echo "Starting Zuul Service with $CONFIGSERVER_URI"
echo "********************************************************"

sleep 60

#dockerize -wait http://eurekaserver:8761 -wait http://configserver:8888
dockerize -wait $EUREKASERVER_URI -wait $CONFIGSERVER_URI

java -Djava.security.egd=file:/dev/./urandom -Dserver.port=$SERVER_PORT   \
     -Deureka.client.serviceUrl.defaultZone=$EUREKASERVER_URI   \
     -Dspring.cloud.config.uri=$CONFIGSERVER_URI                \
     -Dspring.zipkin.baseUrl=$ZIPKIN_URI                        \
     -Dspring.profiles.active=$PROFILE                          \
     -jar /usr/local/zuulservice/@project.build.finalName@.jar
