package com.ynero.ss.event_receiver.persistence;

import com.ynero.ss.event_receiver.domain.Tenant;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CacheTenantRepository {

    @Setter(onMethod_ = {@Autowired})
    private RedisTemplate<String, Tenant> redisTemplate;

    @Setter(onMethod_ = {@Autowired})
    private TenantService tenantService;

    public void save(Tenant tenant) {
        redisTemplate.opsForValue().setIfAbsent(tenant.getTenantId(), tenant);
    }

    public Tenant findById(String tenantId) {
        var tenant = redisTemplate.opsForValue().get(tenantId);
        if (tenant == null){
            tenant = tenantService.findByTenantId(tenantId).get();
            save(tenant);
        }
        return tenant;
    }
}
