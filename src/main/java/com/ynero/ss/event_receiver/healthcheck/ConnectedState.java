package com.ynero.ss.event_receiver.healthcheck;

import org.springframework.boot.actuate.health.Health;
import redis.clients.jedis.Jedis;

public class ConnectedState extends AbstractTolerableRedisConnState {
    private String jedisInfo;

    public ConnectedState(Jedis jedis) {
        super(jedis);
    }

    @Override
    public Health accessHealth(RedisHealthCheckOnFailure ctx) {
        return Health.up().withDetail("info: ", jedisInfo).build();
    }

    @Override
    public TolerableRedisHealthState nextState(RedisHealthCheckOnFailure ctx) {
        return getJedisStatus().isUp() ? null : new HalfFailedState(jedis, ctx.getMaxTolerableDowntime());
    }
}
