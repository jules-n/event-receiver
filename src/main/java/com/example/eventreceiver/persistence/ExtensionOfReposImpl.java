package com.example.eventreceiver.persistence;

import com.example.eventreceiver.domain.Tenant;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class ExtensionOfReposImpl implements ExtensionOfRepos{

    private MongoTemplate mongoTemplate;

    public ExtensionOfReposImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public String findTenantIdByUrl(String url) {
        Query query = new Query();
        query.addCriteria(Criteria.where("urls").is(url));
        var id = mongoTemplate.find(query, Tenant.class).get(0).getTenantId();
        return id;
    }
}
