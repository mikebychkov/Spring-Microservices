FROM mikebychkov/ubuntu-openjdk-11-jre
RUN mkdir -p /usr/local/organizationservice
ADD @project.build.finalName@.jar /usr/local/organizationservice/
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh
