package com.ynero.ss.event_receiver.healthchecks;

import com.ynero.ss.event_receiver.healthchecks.states.ConnectionState;
import com.ynero.ss.event_receiver.healthchecks.states.HalfOpenConnectionState;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.time.Duration;

@Component("redis")
@ConditionalOnProperty(
        name = {"spring.data.redis.tolerance"},
        havingValue = "true"
)
public class TolerableRedisHealthCheck implements HealthIndicator {

    private Jedis jedis;
    @Getter
    @Setter
    private ConnectionState state;

    @Setter(onMethod_ = @Value("${spring.data.redis.max-downtime}"))
    @Getter
    private Duration downtime;

    public TolerableRedisHealthCheck(@Value("${spring.data.redis.host}") String hostName, @Value("${spring.data.redis.port}") int port) {
        jedis = new Jedis(hostName, port);
        state = new HalfOpenConnectionState(jedis);
    }

    @Override
    public Health health() {
        state.onEnter(this);
        return state.access(this);
    }
}
