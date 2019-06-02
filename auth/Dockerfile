FROM openjdk:8-jdk-alpine
EXPOSE 8080
ADD /build/libs/auth-1.0-SNAPSHOT.jar auth.jar
ENTRYPOINT ["java", "-jar", "auth.jar"]