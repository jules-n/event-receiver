package com.ynero.ss.event_receiver.states.healthcheck;

import com.ynero.ss.event_receiver.states.AbstractConnectionState;
import com.ynero.ss.event_receiver.states.IContext;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import redis.clients.jedis.Jedis;

import java.time.Duration;

public class FailedConnectionState extends AbstractConnectionState<Health> {

    @Setter(onMethod_ = @Value("${spring.data.redis.max-downtime}"))
    private Duration downtime;

    public FailedConnectionState(Jedis jedis) {
        super(jedis);
    }

    @Override
    public Health access(IContext context) {
        var nextState = getRedisStatus().isUp() ? new ActiveConnectionState(jedis) : null;
        goNext(context, nextState);
        return Health.down().withDetail("total downtime: ", downtime).build();
    }
}
