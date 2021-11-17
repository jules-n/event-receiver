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
@ConditionalOnProperty(name = {"redis.reestablishing"},
        havingValue = "true")
public class LettuceConfigOnFailure {

    @Setter(onMethod_ = @Value("${spring.data.redis.host}"))
    private String hostName;

    @Setter(onMethod_ = @Value("${spring.data.redis.port}"))
    private int port;

    @Bean
    public CacheService cacheService(){
        LettuceConfig lettuceConfig = new LettuceConfig();
        try {
            Jedis jedis = new Jedis(hostName, port);
            var info = jedis.info();
            jedis.close();
            return lettuceConfig.cacheService();
        } catch (Exception ex) {
            return new NoCacheImpl();
        }
    }
}
