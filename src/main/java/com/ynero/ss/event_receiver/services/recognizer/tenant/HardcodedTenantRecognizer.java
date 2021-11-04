package com.ynero.ss.event_receiver.services.recognizer.tenant;

import com.ynero.ss.event_receiver.persistence.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Primary
public class HardcodedTenantRecognizer implements TenantRecognizer {

    private final static String DEFAULT_TENANT = "hardcoded-recognized-tenant";

    @Autowired
    private TenantService tenantService;

    @Override
    public String getTenant(Map<PATH, String> params) {
        var path = params.keySet().stream().findFirst().get();
        return switch (path) {
            case URL -> tenantService.findTenantIdByUrl(params.get(PATH.URL));
            case TOPIC -> tenantService.findTenantIdByTopic(params.get(PATH.TOPIC));
            default -> getTenant();
        };
    }

    @Override
    public String getTenant() {
        return DEFAULT_TENANT;
    }

}
