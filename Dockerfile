FROM adoptopenjdk/openjdk21:alpine
COPY target/simple-gateway-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8847
ENTRYPOINT ["java", "-jar", "/app.jar"]