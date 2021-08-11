package com.example.eventreceiver.services;

import com.example.eventreceiver.recognizer.TenantRecognizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TenantParameterService {

    @Autowired
    private TenantIdConfig tenantIdConfig;

    @Autowired
    private TenantRecognizer tenantRecognizer;

}
