package com.ynero.ss.event_receiver.config;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import redis.clients.jedis.Jedis;
import services.CacheService;
import services.NoCacheImpl;
import services.SimpleRedisCacheServiceImpl;

@Configuration
public class LettuceConfig {

    @Setter(onMethod_ = @Value("${spring.data.redis.host}"))
    private String hostName;

    @Setter(onMethod_ = @Value("${spring.data.redis.port}"))
    private int port;

    @Bean
    @ConditionalOnProperty(name = {"redis.reestablishing"},
            havingValue = "false")
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(lettuceConnectionFactory());
        template.setHashKeySerializer(RedisSerializer.string());
        template.setHashValueSerializer(RedisSerializer.json());
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.json());
        return template;
    }

    @Bean
    @ConditionalOnProperty(name = {"redis.reestablishing"},
            havingValue = "false")
    public LettuceConnectionFactory lettuceConnectionFactory() {
        var lettuceConnectionFactory = new LettuceConnectionFactory();
        lettuceConnectionFactory.getStandaloneConfiguration()
                .setHostName(hostName);
        lettuceConnectionFactory.getStandaloneConfiguration()
                .setPort(port);
        return lettuceConnectionFactory;
    }

    @Bean
    @ConditionalOnProperty(name = {"redis.reestablishing"},
            havingValue = "false")
    public CacheService cacheService(){
        return new SimpleRedisCacheServiceImpl(redisTemplate());
    }

    @Bean
    @ConditionalOnProperty(name = {"redis.reestablishing"},
            havingValue = "true")
    public CacheService NoCacheService(){
        try {
            Jedis jedis = new Jedis(hostName, port);
            var info = jedis.info("server");
            return new SimpleRedisCacheServiceImpl(redisTemplate());
        } catch (Exception ex) {
            return new NoCacheImpl();
        }
    }

}
