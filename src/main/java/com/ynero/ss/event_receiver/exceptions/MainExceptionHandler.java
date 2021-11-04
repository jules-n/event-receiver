package com.ynero.ss.event_receiver.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class MainExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleItemNotFoundException(
            NoSuchElementException itemNotFoundException,
            WebRequest request
    ){
        ResponseEntity<String> response = ResponseEntity.badRequest().body(
               "You may not have added this URL for the tenant account"
        );
        return response;
    }
}
