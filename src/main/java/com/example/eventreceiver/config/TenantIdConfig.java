package com.example.eventreceiver.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AliasFor;

@Configuration
@EnableConfigurationProperties
@Data
@ConfigurationProperties(prefix = "tenant-id")
public class TenantIdConfig {

    public enum BY{
    HEADER, REQUEST_PARAM;
    }

    private BY by;
    private String headerName;
    private String requestParamName;

}
