package com.ynero.ss.event_receiver.healthcheck;

import org.springframework.boot.actuate.redis.RedisHealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration("redis")
@ConditionalOnProperty(name = {"redis.reestablishing"},
        havingValue = "false")
public class DefaultRedisHealthCheck extends RedisHealthIndicator {
    public DefaultRedisHealthCheck(RedisConnectionFactory connectionFactory) {
        super(connectionFactory);
    }
}
