FROM amazoncorretto:22-alpine

# Se the working directory
WORKDIR /app

# Copy the built JAR file into the container
COPY src/main/resources/application.conf /app/application.conf
COPY build/libs/com.caritos.busy-bee-all.jar /app/app.jar

# Expose the port your Ktor application runs on
EXPOSE 8080

# Command to run the application
CMD ["java", "-Dconfig.file=/app/application.conf","-jar","/app/app.jar"]