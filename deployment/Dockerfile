FROM eclipse-temurin:21-jdk-alpine
RUN adduser -D jorge
WORKDIR /app
VOLUME /tmp
COPY applications/app-service/build/libs/*.jar ms-solicitud.jar
USER jorge
ENTRYPOINT ["java", "-jar", "ms-solicitud.jar"]