package com.ynero.ss.event_receiver.persistence;

import com.ynero.ss.event_receiver.domain.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;

public class MongoTenantService implements TenantService {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public String findTenantIdByTopic(String topic) {
        var tenant = tenantRepository.findByElemMatchInTopics(topic);
        return tenant.getTenantId();
    }

    //*db.tenants.find({topics:{$elemMatch:{$eq:"url-1"}}})*//*
    @Override
    public String findTenantIdByUrl(String url) {
        var tenant = tenantRepository.findByElemMatchInUrls(url);
        return tenant.getTenantId();
    }

    @Override
    public boolean update(Tenant tenant) {
        var update = new Update();

        if (tenant.getTopics() != null)
            update.set("topics", tenant.getTopics());

        if (tenant.getUrls() != null)
            update.set("urls", tenant.getUrls());

        var result = mongoTemplate.updateFirst(new Query(where("tenantId").is(tenant.getTenantId())),
                update,
                Tenant.class);
        return result.wasAcknowledged();
    }

    @Override
    public Tenant save(Tenant tenant) throws Exception {
        if (tenant.getTenantId() != null)
            return tenantRepository.save(tenant);
        throw new Exception();
    }

    @Override
    public Optional<Tenant> findByTenantId(String tenantId) {
        return tenantRepository.findByTenantId(tenantId);
    }
}
