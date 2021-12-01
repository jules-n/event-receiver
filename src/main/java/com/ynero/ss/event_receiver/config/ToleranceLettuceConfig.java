package com.ynero.ss.event_receiver.config;

import com.ynero.ss.event_receiver.persistence.ToleranceCache;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import services.CacheService;

@Configuration
public class ToleranceLettuceConfig {
    @Setter(onMethod_ = @Value("${spring.data.redis.host}"))
    private String hostName;

    @Setter(onMethod_ = @Value("${spring.data.redis.port}"))
    private int port;

    @Bean
    @ConditionalOnProperty(
            name = {"spring.data.redis.tolerance"},
            havingValue = "true"
    )
    public CacheService noCacheService() {
        return new ToleranceCache<String, Object>(hostName, port);
    }
}
