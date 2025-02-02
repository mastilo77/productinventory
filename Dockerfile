FROM openjdk:21

LABEL maintainer="aleksandar96.m@gmail.com"

ARG JAR_FILE=build/libs/*.jar

RUN mkdir -p /opt/app

WORKDIR /opt/app

COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]