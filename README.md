# java_spring_boot_jooq_demo
Demo project with spring boot and jooq

# Configuration
### DB access
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:devdb
User: sa
Password:

### Jooq
mvn clean compile exec:java jooq-code:generate
mvn clean compile jooq-code:generate
mvn clean generate-sources


To generate code:
./gradlew --stop
./gradlew clean generateJooq
./gradlew generateJooq


//                    driver = 'org.postgresql.Driver'
//                    url = 'jdbc:postgresql://localhost:5432/mojabaza'
//                    user = 'admin'
//                    password = 'password'

## PROBLEMS

- database -> start before any action - compilation or application start. 
  Run: 
    - class in IDE
    - mvn exec:java (separate module)
- liquibase -> db scheme -> on demand, not when app starts!
- compilation -> jooq class generation: db has to be on! 

When App starts, then DB starts together -> OK

Start only DB -> OK
Liquibase -> NOK - cannot connect
Compile -> OK
