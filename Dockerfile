# 1 stage: Building app - build
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy maven files
COPY pom.xml
COPY src ./src

# Build
RUN mvn clean package -DskipTests

# 2: Run app
FROM eclipse-temurin:21-jre
WORKDIR /app

# copy build JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Open port
EXPOSE 8080

# Healthcheck
HEALTHCHECK --start-period=30s --interval=30s --timeout=3s --retries=3 \
  CMD curl --fail http://localhost:8080/actuator/health || exit 1

# Run app
ENTRYPOINT ["java", "-jar", "app.jar"