package com.example.eventreceiver.repository;

import com.example.eventreceiver.domain.Tenant;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static org.springframework.data.mongodb.core.query.Criteria.where;

public class TenantMongoExpandedQueries implements TenantExpandedQueries {

    private MongoTemplate mongoTemplate;


    @Override
    public String findTenantIdByTopic(String topic) {
        Query query = new Query();
        query.addCriteria(Criteria.where("topics").elemMatch(new Criteria().is(topic)));
        return mongoTemplate.find(query,Tenant.class).get(0).getTenantId();
    }

    /*db.tenants.find({topics:{$elemMatch:{$eq:"url-1"}}})*/
    @Override
    public String findTenantIdByUrl(String url) {
        Query query = new Query();
        query.addCriteria(Criteria.where("urls").elemMatch(new Criteria().is(url)));
        return mongoTemplate.find(query,Tenant.class).get(0).getTenantId();
    }

    @Override
    public boolean update(Tenant tenant) {
        var update = new Update();

        if (tenant.getTopics() != null)
            update.set("topics", tenant.getTopics());

        if (tenant.getUrls() != null)
            update.set("urls", tenant.getUrls());

        var result = mongoTemplate.updateFirst(new Query(where("tenantId").is(tenant.getTenantId())), update, Tenant.class);
        return result.wasAcknowledged();
    }
}
