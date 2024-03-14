FROM gradle:8.6-jdk21-alpine AS builder
WORKDIR /build-workspace

# Copy.
COPY . .

# Build, Test and publish.
RUN gradle dependencies --refresh-dependencies && gradle clean build

# App image.
FROM amazoncorretto:21-alpine AS runtime
WORKDIR /app
COPY --from=builder build-workspace/app/build/libs/*.jar ./app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
