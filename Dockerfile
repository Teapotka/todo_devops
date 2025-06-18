# Start from OpenJDK base image
FROM openjdk:17-jdk-slim

# Set work directory inside container
WORKDIR /app

# Copy build artifact
COPY target/todo-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 (Spring Boot default)
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
