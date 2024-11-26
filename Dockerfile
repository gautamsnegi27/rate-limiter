# Use the official Eclipse Temurin OpenJDK 17 base image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy the JAR file (built by Maven)
COPY target/rate-limiter-0.0.1-SNAPSHOT.jar rate-limiter.jar

# Expose application port and debug port
EXPOSE 8080 5005

# Set default command
CMD ["java", "-jar", "rate-limiter.jar"]