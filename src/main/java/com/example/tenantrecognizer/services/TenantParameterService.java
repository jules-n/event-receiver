package com.example.tenantrecognizer.services;

import com.example.tenantrecognizer.config.TenantIdConfig;
import com.example.tenantrecognizer.recognizer.TenantRecognizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;

@Service
public class TenantParameterService {

    @Autowired
    private TenantIdConfig tenantIdConfig;

    @Autowired
    private TenantRecognizer tenantRecognizer;

    @Value("${device-service-url}")
    private String url;



    public WebClient addTenant(){
        var webClient =  WebClient.builder().baseUrl(url);
        if(tenantIdConfig.getBy().equals(TenantIdConfig.BY.REQUEST_PARAM))
            webClient.defaultUriVariables(new HashMap<>(){{put(tenantIdConfig.getRequestParamName(),tenantRecognizer.getTenant());}});
        else webClient.defaultHeader(tenantIdConfig.getHeaderName(),tenantRecognizer.getTenant());
        return webClient.build();
    }

}
