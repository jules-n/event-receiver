package com.example.eventreceiver.services.recognizer.tenant;

import com.example.eventreceiver.persistence.TenantRepository;
import com.example.eventreceiver.persistence.TenantService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class HardcodedTenantRecognizer implements TenantRecognizer {

    private final static String DEFAULT_TENANT = "hardcoded-recognized-tenant";

    @Autowired
    private TenantService tenantService;

    @Override
    public String getTenant(Map<PATH, String> params) {
        if (params.containsKey(PATH.URL))
            return tenantService.findTenantIdByUrl(params.get(PATH.URL));

        if (params.containsKey(PATH.TOPIC))
            return tenantService.findTenantIdByTopic(params.get(PATH.TOPIC));

        return getTenant();
    }

    @Override
    public String getTenant() {
        return DEFAULT_TENANT;
    }

}
