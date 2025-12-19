# Stage 2: Create the final lightweight image
FROM eclipse-temurin:21-jre

ENV TZ=Asia/Ho_Chi_Minh

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime \
 && echo $TZ > /etc/timezone

# Create a non-root user
RUN addgroup --system spring && adduser --system --no-create-home --ingroup spring spring
USER spring:spring

WORKDIR /app

COPY --from=builder /app/target/InternShip-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
