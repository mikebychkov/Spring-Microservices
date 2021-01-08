FROM ubuntu
RUN apt -y update && apt -y install
RUN apt -y install openjdk-11-jre-headless
RUN mkdir -p /usr/local/licensingservice
ADD licensing-service-1.jar /usr/local/licensingservice/
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh
