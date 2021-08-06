package com.example.tenantrecognizer.config;

import com.example.tenantrecognizer.recognizer.TenantRecognizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;

@Configuration
public class TenantParameterConfig {

    @Autowired
    private TenantIdConfig tenantIdConfig;

    private TenantRecognizer tenantRecognizer;

    public TenantParameterConfig(){
        tenantRecognizer = new TenantRecognizer();
    }

    @Bean
    public WebClient addTenant(String url){
        var webClient =  WebClient.builder().baseUrl(url);
        if(tenantIdConfig.getBy().equals(TenantIdConfig.BY.REQUEST_PARAM))
            webClient.defaultUriVariables(new HashMap<>(){{put(tenantIdConfig.getRequestParamName(),tenantRecognizer.getTenant());}});
        else webClient.defaultHeader(tenantIdConfig.getHeaderName(),tenantRecognizer.getTenant());
        return webClient.build();
    }

}
