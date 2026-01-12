```markdown
# Java Spring Boot Jooq Demo

This is a demo project that showcases how to use Jooq with Spring Boot to create a simple web application. The
application includes a database schema and some basic CRUD operations.

## Getting Started

To get started, you will need to have the following installed on your system:

* Java 21 or higher
* Maven 3 or higher
* A relational database management system (e.g., MySQL, PostgreSQL)

## Building & Running

To clean old db, create new db from liquibase, generate jooq classes, compile all code and execute unit tests run:
`mvn clean install`

Skip compilation of tests:
`mvn clean install -Dmaven.test.skip=true`
Skip execution of tests but not compilation:
`mvn clean install -DskipTests`

Start the application by running:
`mvn spring-boot:run`
Open a web browser and navigate to `http://localhost:8080/` to access the application

## Database Schema

The database schema for this application is defined in the `src/main/resources/db/changelog/db.changelog-01.yaml` file.
This file uses the Jooq DSL to define the tables, columns, and relationships between them.

Update DB:
`mvn liquibase:update`

Update jooq classes:
`mvn jooq-codegen:generate`

Update jooq classes and compile all:
`mvn clean jooq-codegen:generate compile`

## Testing

Build with execution of integration tests (run in memory H2):
`clean install -DskipITs=true`

Build with execution of unit tests only:
`clean install -DskipITs=false`

Run performance tests:
`mvn gatling:run`

## Project features

List of technical features:

* database history changes managed by liquibase
* unit tests executed separately
* integration tests executed separately
* performance tests executed by Gatling

## App functionalities

The application includes basic CRUD (create, read, update, delete) operations for the `Account`, `Address`, `Role`, and
`LoginHistory` entities.
These operations are defined in the `src/main/java/com/example/demo/controller` package.
The `Account` is generic and can represent: admin, client, manager, customer

## Conclusion

This demo project demonstrates how to use Jooq with Spring Boot to create a simple web application. The project includes
a database schema, CRUD operations, and testing. You can use this project as a starting point for your own projects or
as a reference for learning more about Jooq and Spring Boot.

## Left TODO

- maven cache builds
- embedded postgres for integration tests

```