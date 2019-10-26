FROM openjdk:8-jdk-alpine
EXPOSE 8080
ADD /build/libs/executor-dispatcher-1.0-SNAPSHOT.jar dispatcher.jar
ENTRYPOINT ["java", "-jar", "dispatcher.jar"]