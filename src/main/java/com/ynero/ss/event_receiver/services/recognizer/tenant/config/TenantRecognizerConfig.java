package com.ynero.ss.event_receiver.services.recognizer.tenant.config;

import com.ynero.ss.event_receiver.services.recognizer.tenant.HardcodedTenantRecognizer;
import com.ynero.ss.event_receiver.services.recognizer.tenant.TenantRecognizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TenantRecognizerConfig {

    @Bean
    public TenantRecognizer getTenantRecognizer(){
        return new HardcodedTenantRecognizer();
    }
}
