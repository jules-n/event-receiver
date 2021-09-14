package com.ynero.ss.event_receiver.config;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class LettuceConfig {

    @Setter(onMethod_ = {@Value("${spring.data.redis.host}")})
    private String hostName;

    @Setter(onMethod_ = {@Value("${spring.data.redis.port}")})
    private int port;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(hostName, port));
    }
}
