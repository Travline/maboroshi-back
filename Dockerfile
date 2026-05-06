# Etapa 1: Build (El taller)
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos solo el pom.xml primero para aprovechar la caché de Docker de las dependencias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el código fuente y empaquetamos el proyecto
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Run (El entorno de ejecución ligero)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copiamos solo el archivo .jar generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]