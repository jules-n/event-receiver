package com.example.eventreceiver.repository;

import com.example.eventreceiver.domain.Tenant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends MongoRepository<Tenant, String>{
    Optional<Tenant> findByTenantId(String tenantId);
}
