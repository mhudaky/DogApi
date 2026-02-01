# Dog API Application

This is a Spring Boot application for managing dog data, including images fetched from an external API.

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Build

To build the application, run the following command in the project root directory:

```bash
mvn clean compile
```

## Database Schema Management

This project uses Liquibase for managing database schema changes. The master changelog is located at `src/main/resources/db/changelog/db.changelog-master.xml`.

To apply changes, run the application or use Maven:

```bash
mvn liquibase:update
```

## Run

To run the application locally, use:

```bash
mvn spring-boot:run
```

The application will start on http://localhost:8080 by default.

## Test

To run the unit and integration tests, execute:

```bash
mvn test
```

## API Documentation

Access the OpenAPI specification at: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
