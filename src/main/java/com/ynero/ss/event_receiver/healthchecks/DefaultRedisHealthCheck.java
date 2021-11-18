package com.ynero.ss.event_receiver.healthchecks;

import org.springframework.boot.actuate.redis.RedisHealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

@Component("redis")
@ConditionalOnProperty(
        name = {"spring.data.redis.tolerance"},
        havingValue = "false"
)
public class DefaultRedisHealthCheck extends RedisHealthIndicator {
    public DefaultRedisHealthCheck(RedisConnectionFactory connectionFactory) {
        super(connectionFactory);
    }
}
