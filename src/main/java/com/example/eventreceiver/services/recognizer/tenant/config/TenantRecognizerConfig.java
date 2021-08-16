package com.example.eventreceiver.services.recognizer.tenant.config;

import com.example.eventreceiver.services.recognizer.tenant.HardcodedTenantRecognizer;
import com.example.eventreceiver.services.recognizer.tenant.TenantRecognizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TenantRecognizerConfig {

    @Bean
    public TenantRecognizer getTenantRecognizer(){
        return new HardcodedTenantRecognizer();
    }
}
