# book-management-server

## Technology Stack
* Java
* Spring Boot
* MySql

## Project Setup

start the database using docker-compose:
```sh
docker-compose up -d
```

stop database
```sh
docker-compose down
```

## Build and Run
Provide the steps to build and run the project.

1. Build the project using Gradle:
```shell
./gradlew clean
./gradlew build
```
2. Run the project:
```sh
java -jar build/libs/book-management-server.jar
```
