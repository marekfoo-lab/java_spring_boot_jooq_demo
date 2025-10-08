# java_spring_boot_jooq_demo
Demo project with spring boot and jooq

# Configuration
### DB access
http://localhost:9123/h2-console
JDBC URL: jdbc:h2:tcp://localhost:9123/./data/devdb
User: sa
Password:

### Jooq
mvn liquibase:update
mvn jooq-codegen:generate
mvn clean compile exec:java jooq-code:generate
mvn clean compile jooq-code:generate

### Build package
Skip compilation of tests:
mvn clean install -Dmaven.test.skip=true
Skip execution of tests but not compilation:
mvn install -DskipTests
Run:
java -jar target/jooq-demo-app.jar

To generate code:
./gradlew --stop
./gradlew clean generateJooq
./gradlew generateJooq


## PROBLEMS
