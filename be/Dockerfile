# Stage 1: Build the application with Maven
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# (Tùy chọn) Cài mysql-client nếu bạn cần dùng trong quá trình build/test
RUN apt-get update && apt-get install -y default-mysql-client

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the final lightweight image
FROM eclipse-temurin:21-jre

# 1. Cài đặt múi giờ cho hệ thống (OS Level)
ENV TZ=Asia/Ho_Chi_Minh

# Cài đặt mysql-client cho runtime environment
RUN apt-get update && apt-get install -y default-mysql-client && rm -rf /var/lib/apt/lists/*

# Create a non-root user
RUN addgroup --system spring && adduser --system --no-create-home --ingroup spring spring
USER spring:spring

WORKDIR /app

COPY --from=builder /app/target/InternShip-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# 2. Cài đặt múi giờ cho Java (JVM Level)
ENTRYPOINT ["java", "-Duser.timezone=Asia/Ho_Chi_Minh", "-jar", "app.jar"]