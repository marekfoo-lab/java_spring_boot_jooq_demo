# java_spring_boot_jooq_demo
Demo project with spring boot and jooq

# Configuration
### DB access
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:devdb
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


To generate code:
./gradlew --stop
./gradlew clean generateJooq
./gradlew generateJooq


## PROBLEMS

- database -> start before any action - compilation or application start. 
  Run: 
    - class in IDE
    - mvn exec:java (separate module)
- liquibase -> db scheme -> on demand, not when app starts!
- compilation -> jooq class generation: db has to be on! 

When App starts, then DB starts together -> OK

Start only DB -> OK
Liquibase -> OK 
Compile -> OK
jooq -> OK

Scenarios: 
- Starts App: db start, compile, jooq?
- update db: db start (automatic?), mvn liquibase:update
- build executable jar: mvn clean install -> ERROR - DB ON,
When clean, then after it liquibase has to be run to generate schema and on top of that generate classes

src/main/resources/db/changelog/db.changelog-01.xml
