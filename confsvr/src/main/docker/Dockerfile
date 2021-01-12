FROM ubuntu:20.04
RUN apt -y update && apt -y install
RUN apt -y install openjdk-11-jre-headless
RUN mkdir -p /usr/local/configserver
ADD @project.build.finalName@.jar /usr/local/configserver/
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh
