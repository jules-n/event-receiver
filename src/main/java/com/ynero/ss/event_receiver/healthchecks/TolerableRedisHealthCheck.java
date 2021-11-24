package com.ynero.ss.event_receiver.healthchecks;

import com.ynero.ss.event_receiver.states.ConnectionState;
import com.ynero.ss.event_receiver.states.healthcheck.HalfOpenConnectionState;
import com.ynero.ss.event_receiver.states.IContext;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.time.Duration;

@Component("redis")
@ConditionalOnProperty(
        name = {"spring.data.redis.tolerance"},
        havingValue = "true"
)
public class TolerableRedisHealthCheck implements HealthIndicator, IContext<Health> {

    private Jedis jedis;
    @Getter
    @Setter
    private ConnectionState<Health> state;

    public TolerableRedisHealthCheck(@Value("${spring.data.redis.host}") String hostName, @Value("${spring.data.redis.port}") int port) {
        jedis = new Jedis(hostName, port);
        state = new HalfOpenConnectionState(hostName, port);
    }

    @Override
    public Health health() {
        return state.access(this);
    }
}
