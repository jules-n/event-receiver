package com.ynero.ss.event_receiver.config;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import services.CacheService;
import services.NoCacheImpl;
import services.SimpleRedisCacheServiceImpl;

@Configuration
public class ToleranceLettuceConfig {
    @Setter(onMethod_ = @Value("${spring.data.redis.host}"))
    private String hostName;

    @Setter(onMethod_ = @Value("${spring.data.redis.port}"))
    private int port;

    @Setter(onMethod_ = {@Autowired})
    private LettuceConfig lettuceConfig;

    @Bean
    @ConditionalOnProperty(
            name = {"spring.data.redis.tolerance"},
            havingValue = "true"
    )
    public CacheService noCacheService(){
        try{
            Jedis jedis = new Jedis(hostName, port);
            jedis.info();
            jedis.close();
            return new SimpleRedisCacheServiceImpl(lettuceConfig.redisTemplate());
        } catch (Exception ex) {
            return new NoCacheImpl();
        }
    }
}
