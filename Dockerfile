# Etapa 1: Compilación (Maven construye el proyecto en Render)
FROM maven:3.8.4-openjdk-17-slim AS build
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución (Se crea el contenedor final)
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
