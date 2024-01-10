FROM openjdk:21
COPY ./target/*.jar app.jar
VOLUME /tmp
EXPOSE 80
ENTRYPOINT ["java","-jar","/app.jar"]