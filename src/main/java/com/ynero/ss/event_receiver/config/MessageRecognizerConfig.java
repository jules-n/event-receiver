package com.ynero.ss.event_receiver.config;

import com.ynero.ss.event_receiver.services.recognizer.message.MessageRecognizer;
import com.ynero.ss.event_receiver.services.recognizer.message.FilteringMessageRecognizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageRecognizerConfig {

    @Bean
    public MessageRecognizer getMessageRecognizer(){
        return new FilteringMessageRecognizer();
    }
}
