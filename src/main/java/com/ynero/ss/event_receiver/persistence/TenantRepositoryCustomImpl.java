package com.ynero.ss.event_receiver.persistence;

import com.ynero.ss.event_receiver.domain.Tenant;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class TenantRepositoryCustomImpl implements TenantRepositoryCustom {

    private MongoTemplate mongoTemplate;

    public TenantRepositoryCustomImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public String findTenantIdByUrls(String urls) {
        Query query = new Query();
        query.addCriteria(Criteria.where("urls").is(urls));
        var tenant = mongoTemplate.find(query, Tenant.class).get(0);
        var id = tenant.getTenantId();
        return id;
    }
}
