FROM openjdk:23
EXPOSE 8080
WORKDIR /backend
ADD target/app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]