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


To generate code:
./gradlew --stop
./gradlew clean generateJooq
./gradlew generateJooq


//                    driver = 'org.postgresql.Driver'
//                    url = 'jdbc:postgresql://localhost:5432/mojabaza'
//                    user = 'admin'
//                    password = 'password'