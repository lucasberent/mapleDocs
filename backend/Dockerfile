FROM openjdk:13-jdk-alpine
ARG JAR_FILE=target/mapledocs-*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]