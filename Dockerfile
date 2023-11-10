# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/analyzer-1.0-SNAPSHOT.jar /app/app.jar

# Define the command to run your Java application
CMD ["java", "-jar", "app.jar"]
