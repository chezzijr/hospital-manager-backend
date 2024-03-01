# Backend
This project uses Spring Boot framework as backend.

# Prerequisites
- JRE 21
- JDK 21
- Gradle 8.6
- make

# Get start
- Clone this repository
- Run `make deps` to initialize dependencies
- Run `make run`

# Using docker
- Build image: `docker build -t <image_name> .`
- Run container: `docker run --name <container_name> -p 8080:8080 -d <image_name>`
