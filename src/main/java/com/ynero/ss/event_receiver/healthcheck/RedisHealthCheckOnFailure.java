package com.ynero.ss.event_receiver.healthcheck;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

import java.time.Duration;

@Configuration("redis")
@ConditionalOnProperty(name = {"redis.reestablishing"},
        havingValue = "true")
@Log4j2
public class RedisHealthCheckOnFailure implements HealthIndicator {

    @Setter(onMethod_ = @Value("${spring.data.redis.host}"))
    private String hostName;

    @Setter(onMethod_ = @Value("${spring.data.redis.port}"))
    private int port;

    @Setter(onMethod_ = @Value("${redis.health.max-downtime:5m}"))
    @Getter
    private Duration maxTolerableDowntime;

    private Jedis jedis;
    private TolerableRedisHealthState state;

    public RedisHealthCheckOnFailure() {
        jedis = new Jedis(hostName, port);
        state = new HalfFailedState(jedis, maxTolerableDowntime);
        state.onEnter(this);
    }

    @Override
    public Health health() {
        var nextState = state.nextState(this);
        if (nextState != null) {
            state.onExit(this);
            state = nextState;
            nextState.onEnter(this);
        }
        return state.accessHealth(this);
    }
}
