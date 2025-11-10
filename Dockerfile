# Build stage
FROM maven:3.9-amazoncorretto-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM amazoncorretto:21-alpine
WORKDIR /app
COPY --from=build /app/target/clinic-1.0.0.jar app.jar
ENV JAVA_TOOL_OPTIONS="-Djava.net.preferIPv4Stack=true"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
