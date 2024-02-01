FROM openjdk:17-jdk-slim-buster

LABEL authors="bkaaron"
LABEL maintainer="baaronlubega1@gmail.com"
LABEL version="1.0"
LABEL description="Sms"

COPY build/libs/safe-child-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081

ENTRYPOINT java -jar app.jar

