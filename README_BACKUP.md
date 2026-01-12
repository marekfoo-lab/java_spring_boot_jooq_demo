# java_spring_boot_jooq_demo

Demo project with spring boot and jooq.
Database changes are managed by liquibase.

# Configuration

### DB access

Database is H2 stored in the file.

http://localhost:9123/h2-console
JDBC URL: jdbc:h2:tcp://localhost:9123/./data/devdb
User: sa
Password:

## PROBLEMS

## DevoxxGenie

/test : Write a unit test for this code using JUnit.
/explain : Break down the code in simple terms to help a junior developer grasp its functionality.
/review : Review the selected code, can it be improved or are there any bugs?
/tdg : You are a professional Java developer. Give me a SINGLE FILE COMPLETE java implementation that will pass this
test. Do not respond with a test. Give me only complete code and no snippets. Include imports and use the right package.
/find : Perform semantic search on the project files using RAG and show matching files. (NOTE: The /find command
requires RAG to be enabled in settings)
/help : Display help and available commands for the Genie Devoxx Plugin
/init : Initialize or recreate the DEVOXXGENIE.md file in the project root.
java -jar target/jooq-demo-app.jar
