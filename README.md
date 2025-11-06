# java_spring_boot_jooq_demo
Demo project with spring boot and jooq

# Configuration
### DB access
Database is H2 stored in the file. 

http://localhost:9123/h2-console
JDBC URL: jdbc:h2:tcp://localhost:9123/./data/devdb
User: sa
Password:

### Build package
To clean old db, create new db from liquibase, generate jooq classes, compile all code run:
mvn clean install

Skip compilation of tests:
mvn clean install -Dmaven.test.skip=true
Skip execution of tests but not compilation:
mvn clean install -DskipTests

If separate action are needed to perform:
Update DB:
mvn liquibase:update
Update jooq classes:
mvn jooq-codegen:generate
Update jooq classes and compile all:
mvn clean jooq-codegen:generate compile

### Run App
java -jar target/jooq-demo-app.jar

### Run App in production mode
mvn clean package
Main class: io.quarkus.bootstrap.runner.QuarkusRunner
VM options: -Dquarkus.profile=prod
Program arguments: -Dquarkus.http.port=8080

## PROBLEMS
