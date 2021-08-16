package com.example.eventreceiver.persistence;

import com.example.eventreceiver.domain.Tenant;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface TenantService {

    String findTenantIdByTopic(String topic);

    String findTenantIdByUrl(String url);

    boolean update(Tenant tenant);

    Tenant save(Tenant tenant) throws Exception;

    Optional<Tenant> findByTenantId(String tenantId);
}
