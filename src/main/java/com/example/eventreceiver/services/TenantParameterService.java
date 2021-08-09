package com.example.eventreceiver.services;

import com.example.eventreceiver.config.TenantIdConfig;
import com.example.eventreceiver.recognizer.TenantRecognizer;
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

   /* @Value("${device-service-url}")
    private String url;


    public WebClient addTenant() {
        var webClient = WebClient.builder().baseUrl(url);
        var isMustBeAttachedHeader = tenantIdConfig.getBy().equals(TenantIdConfig.BY.HEADER);
        if (isMustBeAttachedHeader)
            webClient.defaultUriVariables(new HashMap<>() {{
                put(tenantIdConfig.getRequestParamName(), tenantRecognizer.getTenant());
            }});
        else webClient.defaultHeader(tenantIdConfig.getHeaderName(), tenantRecognizer.getTenant());
        return webClient.build();
    }*/

}
