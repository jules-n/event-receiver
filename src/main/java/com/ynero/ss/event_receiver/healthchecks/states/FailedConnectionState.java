package com.ynero.ss.event_receiver.healthchecks.states;

import com.ynero.ss.event_receiver.healthchecks.TolerableRedisHealthCheck;
import org.springframework.boot.actuate.health.Health;
import redis.clients.jedis.Jedis;

public class FailedConnectionState extends AbstractConnectionState {

    public FailedConnectionState(Jedis jedis) {
        super(jedis);
    }

    @Override
    public Health access(TolerableRedisHealthCheck context) {
        return Health.down().withDetail("total downtime: ", context.getDowntime()).build();
    }

    @Override
    public ConnectionState next(TolerableRedisHealthCheck context) {
        return getRedisStatus().isUp() ? new ActiveConnectionState(jedis) : null;
    }
}
