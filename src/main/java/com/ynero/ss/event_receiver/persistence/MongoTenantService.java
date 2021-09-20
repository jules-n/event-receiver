package com.ynero.ss.event_receiver.persistence;

import com.ynero.ss.event_receiver.domain.Tenant;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
public class MongoTenantService implements TenantService {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public String findTenantIdByTopic(String topic) {
        var tenant = tenantRepository.findByElemMatchInTopics(topic);
        if (tenant.isEmpty()) {
            return null;
        }
        return tenant.get().getTenantId();
    }

    //*db.tenants.find({topics:{$elemMatch:{$eq:"url-1"}}})*//*
    @Override
    public String findTenantIdByUrl(String url) {
        var tenant = tenantRepository.findByElemMatchInUrls(url);
        if (tenant.isEmpty()) {
            return null;
        }
        return tenant.get().getTenantId();
    }

    @SneakyThrows
    @Override
    public boolean update(Tenant tenant) {
        var update = new Update();

        if (tenant.getTopics() != null)
            update.set("topics", tenant.getTopics());

        if (tenant.getUrls() != null)
            update.set("urls", tenant.getUrls());

        if (!checkUniquenessOfLinks(tenant)) throw new Exception("Not unique urls/topics");

        var result = mongoTemplate.updateFirst(new Query(where("tenantId").is(tenant.getTenantId())),
                update,
                Tenant.class);
        return result.wasAcknowledged();
    }

    @Override
    public Tenant save(Tenant tenant) throws Exception {
        if (tenant.getTenantId() != null && checkUniquenessOfLinks(tenant))
            return tenantRepository.save(tenant);
        throw new Exception();
    }

    @Override
    public Optional<Tenant> findByTenantId(String tenantId) {
        return tenantRepository.findByTenantId(tenantId);
    }

    private boolean checkUniquenessOfLinks(Tenant tenant) {
        if (tenant.getTopics() != null) {
            for (var topic : tenant.getTopics()) {
                var existingTenant = findTenantIdByTopic(topic);
                if (existingTenant != null && !existingTenant.equals(tenant.getTenantId())) {
                    return false;
                }
            }
        }

        if (tenant.getUrls() != null) {
            for (var url : tenant.getUrls()) {
                var existingTenant = findTenantIdByUrl(url);
                if (existingTenant != null && !existingTenant.equals(tenant.getTenantId())) {
                    return false;
                }
            }
        }
        return true;
    }
}
