# Etapa de compilación
FROM maven:3.8.4-openjdk-11-slim AS build
WORKDIR /app
COPY . .
RUN mvn clean package

# Etapa de despliegue
FROM openjdk:11-slim AS deployment
WORKDIR /app
COPY --from=build /app/target/spring-api-rest-insertar-registros-*.jar spring-demo.jar
ENTRYPOINT ["java", "-Djava.file.encoding=UTF-8", "-jar", "spring-demo.jar"]

# Metadatos adicionales para la imagen
LABEL image.name="imagen-insertar-registros"
LABEL description="Aplicación Spring Demo"