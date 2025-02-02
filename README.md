# product-inventory

## About project

Project is created to manage products in product inventory.

## Created by

<!DOCTYPE html>
<html lang="en">
<span style="font-size:1.1em; font-weight: bold; letter-spacing: 0.5px;">
Name: Aleksandar Mastilovic <br />
Email: aleksandar96.m@gmail.com <br />
Website: <a href="https://mastilovic.github.io" target="_blank" rel="noopener noreferrer">http://mastilovic.github.io</a> <br />
LinkedIn: <a href="https://linkedin.com/in/salexd" target="_blank" rel="noopener noreferrer">www.linkedin.com/in/salexd</a> <br />
Location: Belgrade, Serbia
</span>
</html>

## Technology Stack
#### This project was built using IntelliJ IDEA and uses the following technologies:
* _Java 21_
* _Spring Boot version 3.4.2_
* _H2 embedded database_
* _Gradle_
* _Rest Error Handlers_
* _Unit Tests_
* _Git_
* _Github actions_
* _Docker_
* _Swagger UI & openapi docs_
* _Jakarta Validation_
* _Sonarlint plugin_

## Setup
### Setup for local development

1. Make sure that your system is using Java 21 version (check in cmd: **java --version**)
2. Build application in the root of the project:
    * `./gradlew clean build`
    * or if you want to skip tests: `./gradlew clean build -x test`
3. To run the application, make sure that port **8080** is available:
    * run the application using gradle: `./gradlew bootRun`
4. Alternative way to run the application from **_IntelliJ IDEA_**
    * From root of the project, navigate to `src/main/java`, right click on `ProductinventoryApplication.java` and left click `Run`
5. Test the API using swagger:
    * In your browser access `http://localhost:8080/swagger-ui/index.html` to test API endpoints

### Setup for running application on other devices

1. Install Docker Desktop
2. Create profile on DockerHub
3. Clone GitHub repository:
    * `git clone https://github.com/mastilo77/productinventory.git`
4. Change directory to productinventory:
    * `cd productinventory`
5. Run docker compose:
    * `docker-compose up`
6. Test the API using swagger:
    * In your browser access `http://localhost:8080/swagger-ui/index.html` to test API endpoints
