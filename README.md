# Stock Manager API
The stock-manager is an API that enables manipulation stock quantities of products. In addition to this, user can get current stock information and some statistics about given product, such as top available products or top selling products in given time span.

## Prerequisites
What things you need to install the software and how to install them?

This project requires you to pre-install the following tools:

- Apache Maven
- Java JDK 1.8
- Docker and Docker Compose

## Runtime Dependencies
* Postgres

    
## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Compiling & Installing Project
    
```
mvn install
```

### Running the Tests Locally
Since the application uses Maven, running test is as easy as running one line command.

#### Unit tests
The unit tests in this project will test the consumer, producer and the implementation of the business layer (service). To run these tests you can run the following command

```
mvn test
```

Unit test, mutation test code coverage report 'pit-test' can be found in generated target folder.

#### End to end (E2E) tests
The E2E tests in this project will test whole API endpoints to ensure quality criteria are met defined by API design. There are two E2E test available, these are 'RestlessStockManagerE2e' and 'RestfulStockManagerE2e'. When running these test, Docker container that contains PostgreSQL DB will be automatically stated.

### Running Project Locally
For running it locally, run following command in directory where `docker-compose.yml` file is located in order to provide runtime dependencies (PostgreSQL DB) required for Stock Manager API.

``` 
docker-compose up
```

##### Database
run ```docker-compose up -d```

This will start you an instance of PostgreSQL Database


##### Run Spring Application
After running Docker, run the spring boot application by the following command 

```
mvn spring-boot:run
```

##### Generating API Documentation (Swagger)
After running Stock Manager API, a swagger file is locally available at ```{{url}}/swagger-ui.html``` and should be fully usable as a rest client. 
Example: ```http://localhost:8082/swagger-ui.html```




