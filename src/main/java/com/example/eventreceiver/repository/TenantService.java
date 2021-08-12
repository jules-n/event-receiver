package com.example.eventreceiver.repository;

import com.example.eventreceiver.domain.Tenant;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface TenantService {

    String findTenantIdByTopics(String topic);

    String findTenantIdByUrls(String url);

    boolean update(Tenant tenant);

    Tenant save(Tenant tenant) throws Exception;

    Optional<Tenant> findByTenantId(String tenantId);
}
