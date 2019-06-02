FROM openjdk:8-jdk-alpine
EXPOSE 8080
ADD /build/libs/web-1.0-SNAPSHOT.jar web.jar
ENTRYPOINT ["java", "-jar", "web.jar"]