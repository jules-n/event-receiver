package com.ynero.ss.event_receiver.exceptions.handlers;

import com.ynero.ss.event_receiver.exceptions.entities.RestError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.NoSuchElementException;

@ControllerAdvice
public class MainExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<RestError> handleItemNotFoundException(
            NoSuchElementException itemNotFoundException,
            WebRequest webRequest
    ) {
        var request = ((ServletWebRequest) webRequest).getRequest();
        var url = ((HashMap<String, Object>)request.getAttribute("org.springframework.web.servlet.View.pathVariables")).get("tenantsUrl");
        String message = "No such url: " + url;
        var restError = new RestError(HttpStatus.NOT_FOUND, message, request.getRequestURI(), request.getMethod());
        return buildResponseEntity(restError);
    }

    private ResponseEntity<RestError> buildResponseEntity(RestError error) {
        return new ResponseEntity(error, error.getStatus());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<RestError> handleInternalServerError(
            WebRequest request
    ) {
        String message = "Smth went wrong";
        return buildResponseEntity(new RestError(HttpStatus.INTERNAL_SERVER_ERROR, message));
    }
}
