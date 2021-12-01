package com.ynero.ss.event_receiver.states.healthcheck;

import com.ynero.ss.event_receiver.config.DowntimeConfig;
import com.ynero.ss.event_receiver.states.AbstractConnectionState;
import com.ynero.ss.event_receiver.states.IContext;
import org.springframework.boot.actuate.health.Health;
import redis.clients.jedis.Jedis;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

public class HalfOpenConnectionState extends AbstractConnectionState<Health> {

    final AtomicReference<Instant> failureStartedAt = new AtomicReference<Instant>();

    public HalfOpenConnectionState(String hostName, int port) {
        super(hostName, port);
        failureStartedAt.set(Instant.now());
    }

    @Override
    public Health access(IContext context) {
        var nextState =
                getRedisStatus().isUp() ? new ActiveConnectionState(hostName, port)
                        : getDowntime().compareTo(DowntimeConfig.downtime) > 0?
                        new FailedConnectionState(hostName, port):
                        null;
        goNext(context, nextState);
        return Health.up().withDetail("Redis is unavailable for", getDowntime().getSeconds()+"s").build();
    }

    private Duration getDowntime(){
        return Duration.between(failureStartedAt.get(), Instant.now());
    }
}
