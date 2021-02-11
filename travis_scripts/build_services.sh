echo "Building with travis commit of $BUILD_NAME ..."

mvn -f ./eurekasvr clean package docker:build
mvn -f ./confsvr clean package docker:build
mvn -f ./zuulsvr clean package docker:build
mvn -f ./authentication-service clean package docker:build
mvn -f ./organization-service clean package docker:build
mvn -f ./licensing-service clean package docker:build
