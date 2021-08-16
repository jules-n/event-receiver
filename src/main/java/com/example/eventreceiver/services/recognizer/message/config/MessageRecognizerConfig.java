package com.example.eventreceiver.services.recognizer.message.config;

import com.example.eventreceiver.services.recognizer.message.MessageRecognizer;
import com.example.eventreceiver.services.recognizer.message.SimpleMessageRecognizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageRecognizerConfig {

    @Bean
    public MessageRecognizer getMessageRecognizer(){
        return new SimpleMessageRecognizer();
    }
}
