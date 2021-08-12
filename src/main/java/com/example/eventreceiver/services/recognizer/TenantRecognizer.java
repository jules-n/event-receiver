package com.example.eventreceiver.services.recognizer;

import java.util.Map;

public interface TenantRecognizer {
    String getTenant(Map<PATH, String> params);
    String getTenant();
}
