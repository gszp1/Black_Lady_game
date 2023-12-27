# STAGE 1: SERVER TESTS STAGE
FROM maven:3.8.4-jdk-8 AS builder

RUN mkdir -p test_stage
WORKDIR /test_stage

COPY pom.xml .
COPY src ./src

RUN mvn clean test



# STAGE 2: SERVER BUILD AND RUN STAGE
FROM ubuntu:latest
LABEL authors="gszp1"

RUN apt-get update -y
RUN apt-get install -y openjdk-8-jdk

RUN mkdir -p game_server
WORKDIR /game_server
COPY target/black-lady-game-1.0.0.jar /game_server
COPY pom.xml /game_server

RUN apk --no-cache add maven \
    && mvn clean install \
    && rm -rf /game_server/target

EXPOSE 8080

CMD["java", "-jar", "/game_server/black-lady-game-1.0.0.jar"]