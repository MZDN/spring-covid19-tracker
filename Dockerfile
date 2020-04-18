FROM openjdk:8-jdk-alpine

ADD target/spring*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar" ]
