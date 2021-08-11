package com.example.eventreceiver.services.recognizer;

public class HardcodedTenantRecognizer implements TenantRecognizer{

    private final static String DEFAULT_TENANT = "hardcoded-recognized-tenant";

    @Override
    public String getTenant() {
        return DEFAULT_TENANT;
    }
}
