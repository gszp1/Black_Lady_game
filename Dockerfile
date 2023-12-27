#Dockerfile for running server
FROM ubuntu:latest
LABEL authors="gszp1"

RUN apt-get update -y && \
    apt-get install -y openjdk-8-jdk && \
    apt-get install -y maven

RUN mkdir -p game_server
WORKDIR /game_server
COPY target/black-lady-game-1.0.0.jar /game_server
COPY pom.xml /game_server

EXPOSE 8080

CMD ["java", "-jar", "/game_server/black-lady-game-1.0.0.jar", "jdbc:mysql://test_database_service:3306/Users_database"]