### Docker Deployment Guide for Secure Document Management System

This guide outlines how to deploy the secure document management system using Docker containers and Docker Compose. Docker ensures a consistent environment for the application, making the deployment process smooth and reproducible.

#### Prerequisites

- Docker installed on your system (version 20.10 or higher recommended).
- Docker Compose installed (version 1.29 or higher recommended).
- Basic understanding of Docker commands.

### Dockerfile for Application

The Dockerfile is used to create an image of the Spring Boot application with all dependencies included. Below is the Dockerfile to build the application container:

```dockerfile
# Dockerfile
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the packaged Spring Boot jar into the container
COPY target/secure-docs.jar secure-docs.jar

# Expose the port on which the application will run
EXPOSE 8080

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "secure-docs.jar"]
```

- **Base Image**: The base image used is `openjdk:17-jdk-slim`, which provides the required Java environment for running the Spring Boot application.
- **Working Directory**: The `WORKDIR` is set to `/app`.
- **Jar Copy**: Copies the Spring Boot jar file from the `target` folder to the container.
- **Expose Port**: The application will be accessible on port `8080`.
- **Entry Point**: Runs the Spring Boot application.

### Docker Compose Configuration

Docker Compose is used to orchestrate multiple containers, including the Spring Boot application and H2 database.

```yaml
# docker-compose.yml
version: '3.8'

services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:h2:mem:testdb
      SPRING_DATASOURCE_USERNAME: sa
      SPRING_DATASOURCE_PASSWORD: password
      SPRING_H2_CONSOLE_ENABLED: "true"
    depends_on:
      - db

  db:
    image: "oscarfonts/h2"
    container_name: "h2db"
    ports:
      - "9092:9092"
    environment:
      H2_OPTIONS: '-tcpAllowOthers'
```

- **Services**: Two services are defined, `app` and `db`.
    - **app**: Builds the Spring Boot application container using the Dockerfile. The environment variables set up the H2 database connection for the application.
    - **db**: Uses an H2 database image and makes it accessible on port `9092`. This configuration helps to ensure the database is ready for the Spring Boot app.
- **Ports**: The application container is mapped to port `8080` on the host, while the H2 container is mapped to port `9092`.

### Deployment Steps

1. **Build Docker Images**

    - Navigate to the project root directory, where the Dockerfile and `docker-compose.yml` file are located.
    - Run the command to build Docker images for the Spring Boot application:
      ```sh
      docker-compose build
      ```

2. **Start the Containers**

    - After building the images, you can run the application with:
      ```sh
      docker-compose up
      ```
    - This command will start both the Spring Boot application and the H2 database.

3. **Verify Deployment**

    - Open a web browser and navigate to `http://localhost:8080` to access the application.
    - The H2 console is accessible at `http://localhost:8080/h2-console` for managing the in-memory database.

4. **Shut Down the Containers**

    - To stop the application, run:
      ```sh
      docker-compose down
      ```

### Detailed Deployment Guide

- **Step-by-Step Instructions**: Begin by cloning the repository to your local machine.
  ```sh
  git clone <repository_url>
  cd <repository_folder>
  ```
- **Build the Spring Boot Jar**: Make sure to build the application before running Docker.
  ```sh
  mvn clean package
  ```
  This command generates the `secure-docs.jar` file in the `target` directory, which will be used by Docker to build the container.
- **Configure Environment Variables**: Environment variables are set directly in the Docker Compose file to allow easy configuration changes.

### Summary

Using Docker and Docker Compose simplifies the process of setting up, deploying, and managing the secure document management system. By packaging the entire application and database into Docker containers, deployment becomes consistent across different environments, reducing potential configuration issues. The detailed steps ensure even users with minimal Docker experience can successfully run the application.
