package com.ynero.ss.event_receiver.persistence.config;

import com.ynero.ss.event_receiver.persistence.MongoTenantService;
import com.ynero.ss.event_receiver.persistence.TenantService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TenantServiceConfig {

    @Bean
    TenantService tenantService(){
        return new MongoTenantService();
    }
}
