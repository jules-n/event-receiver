FROM gcr.io/distroless/java:11-debug
SHELL ["/busybox/sh", "-c"]
WORKDIR /app
COPY build/libs/event-receiver.jar /app
CMD ["event-receiver.jar"]