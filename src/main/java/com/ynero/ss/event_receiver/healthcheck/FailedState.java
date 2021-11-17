package com.ynero.ss.event_receiver.healthcheck;

import org.springframework.boot.actuate.health.Health;
import redis.clients.jedis.Jedis;

public class FailedState extends AbstractTolerableRedisConnState {

    public FailedState(Jedis jedis) {
        super(jedis);
    }

    @Override
    public Health accessHealth(RedisHealthCheckOnFailure ctx) {
        return Health.down().withDetail("info: total downtime should be here", "").build();
    }

    @Override
    public TolerableRedisHealthState nextState(RedisHealthCheckOnFailure ctx) {
        return getJedisStatus().isUp() ? new ConnectedState(jedis) : null;
    }
}
