## First stage: complete build environment
#FROM maven:3.8-eclipse-temurin-11-alpine AS builder
#
## add pom.xml and source code
#ADD ./pom.xml pom.xml
#ADD ./src src/
#
## package jar
#RUN mvn clean package
#
## Second stage: minimal runtime environment
#From adoptopenjdk/openjdk11:alpine-jre
#
## copy jar from the first stage
#COPY --from=builder target/zenblog-0.0.1-SNAPSHOT.jar app.jar
#
#EXPOSE 8080
#
#CMD ["java", "-jar", "app.jar"]


FROM openjdk:17-jdk-slim

COPY target/todo-app-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]