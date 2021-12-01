package com.ynero.ss.event_receiver.persistence;

import com.ynero.ss.event_receiver.domain.Tenant;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import services.CacheService;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@Primary
public class CacheTenantService implements TenantService {

    @Setter(onMethod_ = {@Autowired})
    private MongoTenantService tenantService;

    @Setter(onMethod_ = {@Autowired})
    private CacheService<String, Object> cache;

    @Setter(onMethod_ = {@Value("${spring.data.redis.expiration}")})
    private int expirationTime;

    private String tenantIdWithPrefix(String tenantId) {
        String tenantPrefix = "tenant: ";
        return tenantPrefix + tenantId;
    }

    private String tenantIdWithoutPrefix(String tenantId) {
        String tenantPrefix = "tenant: ";
        return tenantId.replaceFirst(tenantPrefix, "");
    }

    private String topicWithPrefix(String topic) {
        String topicPrefix = "tenant:by-topic: ";
        return topicPrefix + topic;
    }

    private String urlWithPrefix(String url) {
        String urlPrefix = "tenant:by-url: ";
        return urlPrefix + url;
    }

    @Override
    public Tenant save(Tenant tenant) throws Exception {
        tenantService.save(tenant);

        var tenantIdWithPrefix = tenantIdWithPrefix(tenant.getTenantId());
        cache.save(tenantIdWithPrefix, tenant, expirationTime, TimeUnit.SECONDS);

        if (tenant.getUrls() != null) {
            tenant.getUrls().forEach(
                    url -> cache.save(urlWithPrefix(url), tenantIdWithPrefix, expirationTime, TimeUnit.SECONDS)
            );
        }

        if (tenant.getTopics() != null) {
            tenant.getTopics().forEach(
                    topic -> cache.save(topicWithPrefix(topic), tenantIdWithPrefix, expirationTime, TimeUnit.SECONDS)
            );
        }

        return tenant;
    }

    @SneakyThrows
    @Override
    public boolean update(Tenant tenant) {
        var isTenantAbsent = findByTenantId(tenant.getTenantId()).isEmpty();
        if (isTenantAbsent) throw new Exception("No such tenant");

        var tenantIdWithPrefix = tenantIdWithPrefix(tenant.getTenantId());
        cache.delete(tenantIdWithPrefix);
        var result = tenantService.update(tenant);
        if (result) {
            cache.save(tenantIdWithPrefix, tenant, expirationTime, TimeUnit.SECONDS);
        }
        return result;
    }

    @Override
    public String findTenantIdByTopic(String topic) {
        var tenantId = (String) cache.get(topicWithPrefix(topic)).orElse(null);
        if (tenantId == null) {
            tenantId = tenantService.findTenantIdByTopic(topic);
            cache.save(topicWithPrefix(topic), tenantIdWithPrefix(tenantId), expirationTime, TimeUnit.SECONDS);
            return tenantId;
        }
        return tenantIdWithoutPrefix(tenantId);
    }

    @Override
    public String findTenantIdByUrl(String url) {
        var tenantId = (String) cache.get(urlWithPrefix(url)).orElse(null);
        if (tenantId == null) {
            tenantId = tenantService.findTenantIdByUrl(url);
            cache.save(urlWithPrefix(url), tenantIdWithPrefix(tenantId), expirationTime, TimeUnit.SECONDS);
            return tenantId;
        }
        return tenantIdWithoutPrefix(tenantId);
    }

    @Override
    public Optional<Tenant> findByTenantId(String tenantId) {
        Tenant tenant = (Tenant) cache.get(tenantIdWithPrefix(tenantId)).orElse(null);
        if (tenant == null) {
            tenant = tenantService.findByTenantId(tenantId).orElseThrow(() -> new NoSuchElementException("No such tenantId: " + tenantId));
            cache.save(tenantIdWithPrefix(tenantId), tenant, expirationTime, TimeUnit.SECONDS);
        }
        return Optional.of(tenant);
    }
}
