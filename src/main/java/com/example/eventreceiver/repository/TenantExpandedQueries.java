package com.example.eventreceiver.repository;

import com.example.eventreceiver.domain.Tenant;

public interface TenantExpandedQueries {

    String findTenantIdByTopic(String topic);
    String findTenantIdByUrl(String url);
    boolean update(Tenant tenant);
}
