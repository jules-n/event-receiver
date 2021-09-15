package com.ynero.ss.event_receiver.persistence;

import com.ynero.ss.event_receiver.domain.Tenant;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class TenantCache {

    @Setter(onMethod_ = {@Autowired})
    private RedisTemplate<String, Object> redisTemplate;

    private final String tenantsHashKey = "TENANTS";
    private final String urlsHashKey = "URLS";
    private final String topicsHashKey = "TOPICS";

    public void save(Tenant tenant) {
        redisTemplate.opsForHash().put(tenantsHashKey, tenant.getTenantId(), tenant);
        tenant.getTopics().forEach(
                topic -> addTopic(topic, tenant.getTenantId())
        );
        tenant.getUrls().forEach(
                url -> addURL(url, tenant.getTenantId())
        );
    }

    public void delete(Tenant tenant) {
        redisTemplate.opsForHash().delete(tenantsHashKey, tenant.getTenantId());
        tenant.getTopics().forEach(
                topic -> redisTemplate.opsForHash().delete(topicsHashKey, topic)
        );
        tenant.getUrls().forEach(
                url -> redisTemplate.opsForHash().delete(urlsHashKey, url)
        );
    }

    public String findByTopic(String topic) {
        return (String) redisTemplate.opsForHash().get(topicsHashKey, topic);
    }

    public void addTopic(String topic, String tenantId) {
        redisTemplate.opsForHash().put(topicsHashKey, topic, tenantId);
    }

    public void addURL(String url, String tenantId) {
        redisTemplate.opsForHash().put(urlsHashKey, url, tenantId);
    }

    public String findByURL(String url) {
        return (String) redisTemplate.opsForHash().get(urlsHashKey, url);
    }

    public Tenant findByTenantId(String tenantId) {
        return (Tenant) redisTemplate.opsForHash().get(tenantsHashKey, tenantId);
    }
}
