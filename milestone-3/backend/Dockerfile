FROM eclipse-temurin:17-jdk
RUN apt-get update && apt-get install -y maven
RUN mkdir app
COPY ./ /app/
RUN cd /app && mvn package
ENV MQTT_HOST=localhost
ENV DB_URL="bolt://localhost:7687"
CMD java -Dspring.neo4j.uri=${DB_URL} -Dmqtt.broker.url=${MQTT_HOST} -jar /app/target/spring-boot-data-neo4j-0.0.1-SNAPSHOT.jar
