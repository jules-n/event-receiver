FROM gcr.io/distroless/java:11-debug
SHELL ["/busybox/sh", "-c"]
WORKDIR /app
COPY build/libs/event-receiver-0.0.1-SNAPSHOT.jar /app
CMD ["event-receiver-0.0.1-SNAPSHOT.jar"]