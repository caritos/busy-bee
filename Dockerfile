FROM openjdk:23-jdk-slim
EXPOSE 8080:8080
RUN mkdir /app
WORKDIR /app
COPY ./build/libs/*.jar /app/app.jar
COPY ./src/main/resources/application.conf /app/application.conf
CMD ["java", "-Dconfig.file=/app/application.conf","-jar","/app/app.jar"]