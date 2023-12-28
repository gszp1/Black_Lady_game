#Dockerfile for running server
FROM ubuntu:latest
LABEL authors="gszp1"

RUN apt-get update -y && \
    apt-get install -y openjdk-8-jdk && \
    apt-get install -y maven

RUN mkdir -p game_server
COPY pom.xml ./game_server
COPY src ./game_server/src
WORKDIR /game_server
RUN find ./src/main/java -type d -name "client" -exec rm -r {} +

RUN mvn clean install -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "/game_server/target/black-lady-game-1.0.0.jar", "jdbc:mysql://database_service:3306/Users_database"]