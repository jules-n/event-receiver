package com.example.eventreceiver.services.recognizer.message;

public class SimpleMessageRecognizer implements MessageRecognizer {
    @Override
    public String getMessage(Object... args) {

        for (Object request : args) {
            if(request!=null){
                return request.toString();
            }
        }

        throw new IllegalArgumentException();
    }
}
