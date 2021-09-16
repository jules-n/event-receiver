package com.ynero.ss.event_receiver.persistence;

import com.ynero.ss.event_receiver.domain.Tenant;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class TenantCache {

    @Setter(onMethod_ = {@Autowired})
    private RedisTemplate<String, Object> redisTemplate;

    @Setter(onMethod_ = {@Value("${spring.data.redis.expiration}")})
    private int expirationTime;

    private String tenantIdWithPrefix(String tenantId) {
        String tenantPrefix = "tenant: ";
        return tenantPrefix + tenantId;
    }

    private String topicWithPrefix(String topic) {
        String topicPrefix = "tenant:by-topic: ";
        return topicPrefix + topic;
    }

    private String urlWithPrefix(String url) {
        String urlPrefix = "tenant:by-url: ";
        return urlPrefix + url;
    }

    public void save(Tenant tenant) {
        redisTemplate.opsForValue().set(tenantIdWithPrefix(tenant.getTenantId()), tenant, expirationTime, TimeUnit.SECONDS);
        tenant.getTopics().forEach(
                topic -> addTopic(topic, tenant.getTenantId())
        );
        tenant.getUrls().forEach(
                url -> addURL(url, tenant.getTenantId())
        );
    }

    public void delete(Tenant tenant) {
        redisTemplate.delete(tenantIdWithPrefix(tenant.getTenantId()));
        tenant.getTopics().forEach(
                topic -> redisTemplate.delete(topicWithPrefix(topic))
        );
        tenant.getUrls().forEach(
                url -> redisTemplate.delete(urlWithPrefix(url))
        );
    }

    public String findByTopic(String topic) {
        return (String) redisTemplate.opsForValue().get(topicWithPrefix(topic));
    }

    public void addTopic(String topic, String tenantId) {
        redisTemplate.opsForValue().set(topicWithPrefix(topic), tenantIdWithPrefix(tenantId), expirationTime, TimeUnit.SECONDS);
    }

    public void addURL(String url, String tenantId) {
        redisTemplate.opsForValue().set(urlWithPrefix(url), tenantIdWithPrefix(tenantId), expirationTime, TimeUnit.SECONDS);
    }

    public String findByURL(String url) {
        return (String) redisTemplate.opsForValue().get(urlWithPrefix(url));
    }

    public Tenant findByTenantId(String tenantId) {
        return (Tenant) redisTemplate.opsForValue().get(tenantIdWithPrefix(tenantId));
    }
}
