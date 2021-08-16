package com.example.eventreceiver.persistence.config;

import com.example.eventreceiver.persistence.MongoTenantService;
import com.example.eventreceiver.persistence.TenantService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TenantServiceConfig {

    @Bean
    TenantService tenantService(){
        return new MongoTenantService();
    }
}
