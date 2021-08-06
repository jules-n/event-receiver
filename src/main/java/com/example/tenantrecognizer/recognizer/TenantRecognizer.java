package com.example.tenantrecognizer.recognizer;

import org.springframework.stereotype.Service;

@Service
public class TenantRecognizer {
    public String getTenant(){
        return "hardcoded-recognized-tenant";
    }
}
