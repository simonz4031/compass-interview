FROM openjdk:8-alpine
MAINTAINER Simon simonz4031@gmail.com

COPY target/compass-interview-0.0.1-SNAPSHOT.jar /compassinterview.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/compassinterview.jar"]
