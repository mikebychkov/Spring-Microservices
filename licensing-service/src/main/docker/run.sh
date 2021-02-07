#!/bin/sh
echo "********************************************************"
echo "Starting Licensing Service"
echo "********************************************************"

sleep 60

#dockerize -wait http://eurekaserver:8761 -wait http://configserver:8888 -wait http://database:5432
#dockerize -wait http://kafkaserver:2181 -wait http://redis:6379

dockerize -wait $EUREKASERVER_URI -wait $CONFIGSERVER_URI -wait $DATABASESERVER_URI
dockerize -wait $KAFKASERVER_URI -wait $REDIS_URI

java -Djava.security.egd=file:/dev/./urandom -Dserver.port=$SERVER_PORT   \
     -Deureka.client.serviceUrl.defaultZone=$EUREKASERVER_URI             \
     -Dspring.cloud.config.uri=$CONFIGSERVER_URI                          \
     -Dspring.cloud.stream.kafka.binder.zkNodes=$KAFKASERVER_URI          \
     -Dspring.cloud.stream.kafka.binder.brokers=$ZKSERVER_URI             \
     -Dspring.zipkin.baseUrl=$ZIPKIN_URI                                  \
     -Dspring.profiles.active=$PROFILE -jar /usr/local/licensingservice/@project.build.finalName@.jar
