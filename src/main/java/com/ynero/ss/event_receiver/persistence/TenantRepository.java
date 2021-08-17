package com.ynero.ss.event_receiver.persistence;

import com.ynero.ss.event_receiver.domain.Tenant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends MongoRepository<Tenant, String>, ExtensionOfTenantRepository {
    Optional<Tenant> findByTenantId(String tenantId);

    @Query("{urls:{$elemMatch:{$eq:?0}}}")
    Tenant findByElemMatchInUrls(String url);

    @Query("{topics:{$elemMatch:{$eq:?0}}}")
    Tenant findByElemMatchInTopics(String topic);
}
