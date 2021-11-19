package com.ynero.ss.event_receiver.states.healthcheck;

import com.ynero.ss.event_receiver.states.AbstractConnectionState;
import com.ynero.ss.event_receiver.states.IContext;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import redis.clients.jedis.Jedis;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

public class HalfOpenConnectionState extends AbstractConnectionState<Health> {

    final AtomicReference<Instant> failureStartedAt = new AtomicReference<Instant>();

    @Setter(onMethod_ = @Value("${spring.data.redis.max-downtime}"))
    private Duration downtime;

    public HalfOpenConnectionState(Jedis jedis) {
        super(jedis);
        failureStartedAt.set(Instant.now());
    }

    @Override
    public Health access(IContext context) {
        var nextState =
                getRedisStatus().isUp() ? new ActiveConnectionState(jedis)
                        : getDowntime().compareTo(downtime) > 0?
                        new FailedConnectionState(jedis):
                        null;
        goNext(context, nextState);
        return Health.up().withDetail("Redis is unavailable for", getDowntime().getSeconds()+"s").build();
    }

    private Duration getDowntime(){
        return Duration.between(failureStartedAt.get(), Instant.now());
    }
}
