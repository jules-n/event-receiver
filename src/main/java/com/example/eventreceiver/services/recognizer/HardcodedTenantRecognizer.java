package com.example.eventreceiver.services.recognizer;

import com.example.eventreceiver.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class HardcodedTenantRecognizer implements TenantRecognizer{

    private final static String DEFAULT_TENANT = "hardcoded-recognized-tenant";

    /*@Autowired
    private TenantRepository tenantRepository;*/

    @Override
    public String getTenant(Map<PATH, String> params) {
      /*  if (params.containsKey(PATH.URL))
            return tenantRepository.findTenantIdByUrl(params.get(PATH.URL));

        if(params.containsKey(PATH.TOPIC))
            return tenantRepository.findTenantIdByTopic(params.get(PATH.TOPIC));*/

        return getTenant();
    }

    @Override
    public String getTenant() {
        return DEFAULT_TENANT;
    }

}
