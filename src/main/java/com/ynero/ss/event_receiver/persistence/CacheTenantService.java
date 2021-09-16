package com.ynero.ss.event_receiver.persistence;

import com.ynero.ss.event_receiver.domain.Tenant;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Primary
public class CacheTenantService implements TenantService {

    @Setter(onMethod_ = {@Autowired})
    private MongoTenantService tenantService;

    @Setter(onMethod_ = {@Autowired})
    private TenantCache cache;

    @Override
    public Tenant save(Tenant tenant) throws Exception {
        tenantService.save(tenant);
        cache.save(tenant);
        return tenant;
    }

    @SneakyThrows
    @Override
    public boolean update(Tenant tenant) {
        var oldTenant = findByTenantId(tenant.getTenantId()).get();
        cache.delete(oldTenant);
        var result = tenantService.update(tenant);
        if (result) {
            cache.save(tenant);
        }
        return result;
    }

    @Override
    public String findTenantIdByTopic(String topic) {
        var tenantId = cache.findByTopic(topic);
        if (tenantId == null) {
            tenantId = tenantService.findTenantIdByTopic(topic);
            cache.addTopic(topic, tenantId);
        }
        return tenantId;
    }

    @Override
    public String findTenantIdByUrl(String url) {
        var tenantId = cache.findByURL(url);
        if (tenantId == null) {
            tenantId = tenantService.findTenantIdByTopic(url);
            cache.addURL(url, tenantId);
        }
        return tenantId;
    }

    @SneakyThrows
    @Override
    public Optional<Tenant> findByTenantId(String tenantId) {
        var tenant = cache.findByTenantId(tenantId);
        if (tenant == null) {
            tenant = tenantService.findByTenantId(tenantId).get();
            cache.save(tenant);
        }
        return Optional.of(tenant);
    }
}
