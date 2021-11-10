FROM openjdk:17.0.1
WORKDIR /app
COPY build/libs/event-receiver.jar /app
CMD ["java", "event-receiver.jar"]
