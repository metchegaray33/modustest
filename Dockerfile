FROM openjdk:11

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","-Djava.net.preferIPv4Stack=true","/app.jar"]