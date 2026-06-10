# Use the official Java 21 image
FROM eclipse-temurin:21-jdk

# Create a directory for the app
WORKDIR /app

# Copy the source code and the Maven wrapper
COPY . .

# Build the project
RUN apt-get update && apt-get install -y maven
RUN mvn clean install

# Render provides the PORT environment variable, so we use it to run the application
EXPOSE 8080

# Launch the jar
CMD ["java", "-jar", "target/shared-calendar-1.0-SNAPSHOT.jar"]
