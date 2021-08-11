package com.example.eventreceiver.recognizer;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface TenantRecognizer {

    default String getTenant(Map<String, String> criteria){
        return "hardcoded-recognized-tenant";
    }
}
