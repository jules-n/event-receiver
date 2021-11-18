package com.ynero.ss.event_receiver.healthchecks.states;

import com.ynero.ss.event_receiver.healthchecks.TolerableRedisHealthCheck;
import org.springframework.boot.actuate.health.Health;
import redis.clients.jedis.Jedis;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

public class HalfOpenConnectionState extends AbstractConnectionState{

    final AtomicReference<Instant> failureStartedAt = new AtomicReference<Instant>();

    public HalfOpenConnectionState(Jedis jedis) {
        super(jedis);
        failureStartedAt.set(Instant.now());
    }

    @Override
    public Health access(TolerableRedisHealthCheck context) {
        return Health.up().withDetail("Redis is unavailable for: ", getDowntime().getSeconds()).build();
    }

    @Override
    public ConnectionState next(TolerableRedisHealthCheck ctx) {
        var status = getRedisStatus();
        var exceedsMaxDowntime = getDowntime().compareTo(ctx.getDowntime()) > 0;
        return status.isUp() ? new ActiveConnectionState(jedis)
                : exceedsMaxDowntime ? new FailedConnectionState(jedis)
                : null;
    }

    private Duration getDowntime(){
        return Duration.between(failureStartedAt.get(), Instant.now());
    }
}
