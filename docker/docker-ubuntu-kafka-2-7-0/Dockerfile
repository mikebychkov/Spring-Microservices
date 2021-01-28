FROM mikebychkov/ubuntu-openjdk-11-jre
RUN wget https://downloads.apache.org/kafka/2.7.0/kafka_2.13-2.7.0.tgz
RUN tar -xzf kafka_2.13-2.7.0.tgz
RUN mv kafka_2.13-2.7.0 /usr/local/kafka-server
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh

