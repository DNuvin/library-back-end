FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copy JAR
COPY target/library-0.0.1-SNAPSHOT.jar app.jar

# Copy the JaCoCo report outside the JAR
# Flattened or full report from Maven target/site/jacoco
COPY target/site/jacoco /app/static/jacoco

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
