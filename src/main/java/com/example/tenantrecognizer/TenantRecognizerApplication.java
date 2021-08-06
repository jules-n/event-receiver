package com.example.tenantrecognizer;

import com.example.tenantrecognizer.config.TenantIdConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(TenantIdConfig.class)
public class TenantRecognizerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TenantRecognizerApplication.class, args);
	}

}
