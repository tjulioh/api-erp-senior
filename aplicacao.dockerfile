FROM maven:3-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests
FROM amazoncorretto:21-alpine
COPY --from=build /app/api-erp-senior/target/aplicacao.jar aplicacao.jar
ENTRYPOINT ["java", "-jar", "aplicacao.jar","-XX:+UseSerialGC","-Xss512k","-XX:MaxRAM=96m"]