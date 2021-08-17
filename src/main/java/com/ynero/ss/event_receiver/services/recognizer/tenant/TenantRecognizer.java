package com.ynero.ss.event_receiver.services.recognizer.tenant;

import java.util.Map;

public interface TenantRecognizer {
    String getTenant(Map<PATH, String> params);
    String getTenant();
}
