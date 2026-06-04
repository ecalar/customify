# Usamos una imagen oficial de Java 17 súper ligera basada en Alpine Linux
FROM eclipse-temurin:17-jdk-alpine

# Creamos un volumen temporal (útil para Tomcat dentro de Spring Boot)
VOLUME /tmp

# Copiamos el archivo .jar compilado desde la carpeta target al contenedor
COPY target/*.jar app.jar

# Exponemos el puerto 8080 (opcional, pero buena práctica como documentación)
EXPOSE 8080

# Comando para ejecutar la aplicación cuando arranque el contenedor
ENTRYPOINT ["java", "-jar", "/app.jar"]