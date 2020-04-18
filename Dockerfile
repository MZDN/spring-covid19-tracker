FROM openjdk:11-jdk-slim

ADD target/spring-boot*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar" ]
