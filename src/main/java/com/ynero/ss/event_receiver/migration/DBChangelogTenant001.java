package com.ynero.ss.event_receiver.migration;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import com.ynero.ss.event_receiver.domain.DeviceData;
import com.ynero.ss.event_receiver.domain.Tenant;
import domain.SendingParameters;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@ChangeLog
@Log4j2
public class DBChangelogTenant001 {

    @ChangeSet(order = "001", id = "creating system tenant", author = "ynero")
    public void insertSystemTenant(MongoDatabase mongo) {
        var collection = mongo.getCollection(Tenant.COLLECTION_NAME);
        DeviceData deviceData = new DeviceData();
        deviceData.setDeviceIdAlias("deviceId");
        deviceData.setEventTypeAlias("type");
        SendingParameters parameters = new SendingParameters();
        parameters.setSendingClassName("com.ynero.sender.DefaultDeviceSender");
        parameters.setParameters(
                new HashMap<>(){{
                    put("host", "localhost");
                    put("port", "8080");
                }}
        );
        Set<String> urls = new HashSet<>();
        urls.add("default");
        Set<String> topics = new HashSet<>();
        topics.add("default");
        Document document = new Document();
        document.put("tenantId", "single-system");
        document.put("topics", urls);
        document.put("urls", topics);
        document.put("deviceData", deviceData);
        document.put("parameters", parameters);
        mongo.getCollection(Tenant.COLLECTION_NAME).insertOne(document);
    }
}