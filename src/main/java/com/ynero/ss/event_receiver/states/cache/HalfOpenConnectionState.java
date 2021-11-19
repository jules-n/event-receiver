package com.ynero.ss.event_receiver.states.cache;

import com.ynero.ss.event_receiver.states.AbstractConnectionState;
import com.ynero.ss.event_receiver.states.IContext;
import com.ynero.ss.event_receiver.states.healthcheck.ActiveConnectionState;
import com.ynero.ss.event_receiver.states.healthcheck.FailedConnectionState;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.Jedis;
import services.CacheService;
import services.NoCacheImpl;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

public class HalfOpenConnectionState<K, V> extends AbstractConnectionState<CacheService<K,V>> {
    final AtomicReference<Instant> failureStartedAt = new AtomicReference<Instant>();

    @Setter(onMethod_ = @Value("${spring.data.redis.max-downtime}"))
    private Duration downtime;

    public HalfOpenConnectionState(Jedis jedis) {
        super(jedis);
        failureStartedAt.set(Instant.now());
    }

    @Override
    public CacheService<K, V> access(IContext context) {
        var nextState =
                getRedisStatus().isUp() ? new ActiveConnectionState(jedis)
                        : getDowntime().compareTo(downtime) > 0?
                        new FailedConnectionState(jedis):
                        null;
        goNext(context, nextState);
        return new NoCacheImpl<K, V>();
    }

    private Duration getDowntime(){
        return Duration.between(failureStartedAt.get(), Instant.now());
    }
}
