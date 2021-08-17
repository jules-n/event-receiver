package com.example.eventreceiver.persistence;

import com.example.eventreceiver.domain.Tenant;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class ExtensionOfMongoTenantRepository implements ExtensionOfTenantRepository {

    private MongoTemplate mongoTemplate;

    public ExtensionOfMongoTenantRepository(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public String findTenantIdByUrls(String urls) {
        Query query = new Query();
        query.addCriteria(Criteria.where("urls").is(urls));
        var id = mongoTemplate.find(query, Tenant.class).get(0).getTenantId();
        return id;
    }
}
