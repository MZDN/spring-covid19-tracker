FROM openjdk:11-jdk-slim

ADD target/spring*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar" ]
