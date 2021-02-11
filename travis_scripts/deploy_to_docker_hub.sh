echo "Pushing service docker images to docker hub ...."

echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin

docker push mikebychkov/tmx-eurekasvr:$BUILD_NAME
docker push mikebychkov/tmx-confsvr:$BUILD_NAME
docker push mikebychkov/tmx-zuulsvr:$BUILD_NAME
docker push mikebychkov/tmx-authentication-service:$BUILD_NAME
docker push mikebychkov/tmx-organization-service:$BUILD_NAME
docker push mikebychkov/tmx-licensing-service:$BUILD_NAME
