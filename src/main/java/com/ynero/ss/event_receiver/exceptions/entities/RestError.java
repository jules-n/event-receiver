package com.ynero.ss.event_receiver.exceptions.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@ToString
@Getter
public class RestError {
    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String path;
    private String method;

    private RestError() {
        timestamp = LocalDateTime.now();
    }

    RestError(HttpStatus status) {
        this();
        this.status = status;
    }

    public RestError(HttpStatus status, String message) {
        this();
        this.status = status;
        this.message = message;
    }

    public RestError(HttpStatus status, String message, String path) {
        this();
        this.status = status;
        this.message = message;
        this.path = path;
    }

    public RestError(HttpStatus status, String message, String path, String method) {
        this();
        this.status = status;
        this.message = message;
        this.path = path;
        this.method = method;
    }
}
