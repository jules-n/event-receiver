package com.ynero.ss.event_receiver.healthchecks.states;

import com.ynero.ss.event_receiver.healthchecks.TolerableRedisHealthCheck;
import org.springframework.boot.actuate.health.Health;
import redis.clients.jedis.Jedis;

public class ActiveConnectionState extends AbstractConnectionState{
    public ActiveConnectionState(Jedis jedis) {
        super(jedis);
    }

    @Override
    public Health access(TolerableRedisHealthCheck context) {
        return Health.up().withDetail("info", info).build();
    }

    @Override
    public ConnectionState next(TolerableRedisHealthCheck ctx) {
        return getRedisStatus().isUp() ? null : new HalfOpenConnectionState(jedis);
    }
}
