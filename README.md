# exchange-rate-challenge
### Requirements (Prerequisites)
Make sure, you have these requirements installed:
- Java 11
- Maven
- Docker (optional)
- docker-compose (optional)
### Build
You can build the application with the next commands:
```
$ mvn clean
$ mvn package
```
To build a Docker image(you must have Docker installed on your computer):
```
$ mvn jib:dockerBuild
```
### Launching
#### Jar
```
$ java -jar <artifact>.jar
```
#### Docker
```
$ docker-compose up -d
```
### Running tests
The simplest way to run the tests:
```
$ mvn clean
$ mvn compile test-compile
$ mvn test
```
