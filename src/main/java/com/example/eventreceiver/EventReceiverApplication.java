package com.example.eventreceiver;

import com.example.eventreceiver.config.TenantIdConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(TenantIdConfig.class)
public class EventReceiverApplication {

    public static void main(String[] args) {
		SpringApplication.run(EventReceiverApplication.class, args);
	}

}
