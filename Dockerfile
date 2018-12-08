FROM openjdk:8-jdk-alpine
#FROM openjdk:8-jre

MAINTAINER jonlorusso@gmail.com

#ADD target/lib /app/lib

ARG JAR_FILE
ADD target/${JAR_FILE} /app/myservice.jar

ENTRYPOINT ["/usr/bin/java", "-jar", "/app/myservice.jar"]
