package com.ynero.ss.event_receiver.healthcheck;

import org.springframework.boot.actuate.health.Health;
import redis.clients.jedis.Jedis;

import java.time.Duration;
import java.time.Instant;

public class HalfFailedState extends AbstractTolerableRedisConnState {
    private Instant startedAt;
    private Duration maxDowntime;

    public HalfFailedState(Jedis jedis, Duration maxDowntime) {
        super(jedis);
        startedAt = Instant.now();
        this.maxDowntime = maxDowntime;
    }

    @Override
    public Health accessHealth(RedisHealthCheckOnFailure ctx) {
        return Health.up().withDetail("info: ", jedisInfo + getDowntime()).build();
    }

    @Override
    public TolerableRedisHealthState nextState(RedisHealthCheckOnFailure ctx) {
        var jedisStatus = getJedisStatus();
        var exceedsMaxDowntime = getDowntime().compareTo(maxDowntime) > 0;
        return jedisStatus.isUp() ? new ConnectedState(jedis)
                : exceedsMaxDowntime ? new FailedState(jedis)
                : null;
    }

    private Duration getDowntime() {
        return Duration.between(startedAt, Instant.now());
    }
}
