package com.example.eventreceiver.repository.config;

import com.example.eventreceiver.repository.MongoTenantService;
import com.example.eventreceiver.repository.TenantService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TenantServiceConfig {

    @Bean
    TenantService tenantService(){
        return new MongoTenantService();
    }
}
