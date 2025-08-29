# Use lightweight JDK runtime
FROM eclipse-temurin:17-jdk-jammy

# Set working directory
WORKDIR /app

# Copy the already-built jar into the image
COPY target/library-0.0.1-SNAPSHOT.jar app.jar

# Expose the app port
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
