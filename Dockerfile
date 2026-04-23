# Usamos una imagen de Java 17
FROM eclipse-temurin:17-jdk-alpine
# Creamos un directorio para la app
WORKDIR /app
# Copiamos el archivo JAR que generaste con el "BUILD SUCCESS"
COPY target/*.jar app.jar
# Comando para ejecutar la aplicación
ENTRYPOINT ["java","-jar","/app.jar"]
