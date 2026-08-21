# BUILD
FROM gradle:9.5.1-jdk21-ubi9 AS build
WORKDIR /app
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle
COPY src ./src
RUN gradle bootJar --no-daemon

# RUN
FROM bellsoft/liberica-openjdk-debian:21
WORKDIR /app
COPY --from=build /app/build/libs/library-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
