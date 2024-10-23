FROM openjdk:17

# Set the working directory in the container
WORKDIR /history-service

# Sao chép file JAR của ứng dụng vào container
COPY target/*.jar history-service.jar

# Expose cổng mà ứng dụng Spring Boot sẽ chạy
EXPOSE 8082

# Câu lệnh để chạy ứng dụng
CMD ["java", "-jar", "history-service.jar"]