package com.ynero.ss.event_receiver.states.healthcheck;

import com.ynero.ss.event_receiver.states.AbstractConnectionState;
import com.ynero.ss.event_receiver.states.IContext;
import org.springframework.boot.actuate.health.Health;
import redis.clients.jedis.Jedis;

public class ActiveConnectionState extends AbstractConnectionState<Health> {
    public ActiveConnectionState(Jedis jedis) {
        super(jedis);
    }

    @Override
    public Health access(IContext context) {
        var nextState = getRedisStatus().isUp() ? null : new HalfOpenConnectionState(jedis);
        goNext(context, nextState);
        return Health.up().withDetail("info", info).build();
    }
}
