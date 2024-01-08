Black-Lady Game Project.
The goal of this project is to implement black lady game as a client-server service written in Java 8.
1. Used Technologies.
   Following technologies were used to create this project:
   - Java 8
   - JUnit
   - JavaFX
   - JavaDoc
   - Maven
   - Docker
   - Docker-compose
   - MySQL
2. Running Project.
   Both server application and database for user login and registration are run using docker containers. Client application is run like standard Java program.
   Creating and running these containers are done through already written docker-compose files. The database container is made using latest MySQL image from
   remote docker repositories, and procedure for creating server image is defined in Dockerfile. The service tests are run using two containers containing
   testing database and server respectively.
   Commands for running Containers:
   -Creating container and/or running service: docker compose -f docker-compose.yaml up
   -Creating tests container and/or running tests service: docker compose -f docker-compose.tests.yaml up
   -Stopping service: docker compose down
3. Application specifications.
   3.1 Server architecture:
       Server consists of mutliple threads running simultaneously. Main thread listens for incoming connections. Each connection with client is handled independently,
       by one thread listening for messages from client. Tasks given by users are handled by one thread called message processor. 
   3.2 Server-Client communication:
     - User and server communicate using messages send via Java streams, where client sends requests and server answers with responses.
     - Connection flow: client -> request -> input listener -> message processor -> response -> client.
   3.4 Login procedure:
       After establishing connection with server, client can login in order to be able to connect with game rooms. When clients wants to login, he sends request to server
       containing user's email and password. Server checks if record of such user exists in database, and if does, validates given credentials. After that, server
       sends response which states if user has successfuly logged in or not. When user connects without login, he is given ID consisting of his IP address and port number.
       After successful login request, server changes that ID to actual ID from database.
   3.5 Registration procedure:
       If application user decides to create new account, he must send a request to server containing email, username, password, and confirmation of that password. Server
       checks if such user already exists and validates credentials. After that sends response with information about registration result.
   
![image](https://github.com/gszp1/Black_Lady_game/assets/117859917/9b8829a1-7070-48b9-ab37-f6bed4ff9de2)

