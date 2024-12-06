FROM gradle:latest as builder
LABEL authors="devinfritz"

WORKDIR /app
COPY . .
RUN gradle clean build --parallel

FROM openjdk:21-slim
WORKDIR /app

COPY --from=builder /app/build/libs/backend-all.jar app.jar

ENTRYPOINT ["java", "-jar", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "/app/app.jar"]