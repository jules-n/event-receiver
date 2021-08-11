package com.example.eventreceiver.services.recognizer.config;

import com.example.eventreceiver.services.recognizer.HardcodedTenantRecognizer;
import com.example.eventreceiver.services.recognizer.TenantRecognizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TenantRecognizerConfig {

    @Bean
    public TenantRecognizer getTenantRecognizer(){
        return new HardcodedTenantRecognizer();
    }
}
