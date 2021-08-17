package com.example.eventreceiver.services.recognizer.message;

public class FilteringMessageRecognizer implements MessageRecognizer {
    @Override
    public Object getMessage(Object... args) {

        for (Object request : args) {
            if(request!=null){
                return request;
            }
        }

        throw new IllegalArgumentException();
    }
}
