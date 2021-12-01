package com.ynero.ss.event_receiver.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.Jedis;
import services.CacheService;
import services.NoCacheImpl;
import services.SimpleRedisCacheServiceImpl;

@Configuration
public class LettuceConfig {

    private final String hostName;

    private final int port;


    public LettuceConfig(@Value("${spring.data.redis.host}") String hostName, @Value("${spring.data.redis.port}") int port) {
        this.hostName = hostName;
        this.port = port;
    }

    @Bean(autowireCandidate = true)
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory());
        redisTemplate.setHashValueSerializer(RedisSerializer.json());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(RedisSerializer.json());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
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
        lettuceConnectionFactory.afterPropertiesSet();
        return lettuceConnectionFactory;
    }
  
    @ConditionalOnProperty(
            name = {"spring.data.redis.tolerance"},
            havingValue = "false"
    )
    public CacheService defaultCacheService(){
        return new SimpleRedisCacheServiceImpl<String, Object>(redisTemplate());
    }

}
