FROM openjdk:23-jdk-slim
WORKDIR /app
COPY ./build/libs/com.caritos.busy-bee-all.jar /app/app.jar
EXPOSE 8080:8080
ENTRYPOINT ["java","-jar","/app/app.jar"]