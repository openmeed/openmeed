FROM openjdk:8-jdk-alpine

ENV server.max-http-header-size=16384 \
    server.port=8080

WORKDIR /tmp
COPY notification-service-boot-0.1.0-BUILD-SNAPSHOT.jar .

CMD ["java", "-jar", "notification-service-boot-0.1.0-BUILD-SNAPSHOT.jar"]
