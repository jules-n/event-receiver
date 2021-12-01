package com.ynero.ss.event_receiver.states.cache;

import com.ynero.ss.event_receiver.states.AbstractConnectionState;
import com.ynero.ss.event_receiver.config.DowntimeConfig;
import com.ynero.ss.event_receiver.states.IContext;
import redis.clients.jedis.Jedis;
import services.CacheService;
import services.NoCacheImpl;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

public class HalfOpenConnectionState<K, V> extends AbstractConnectionState<CacheService<K,V>> {
    final AtomicReference<Instant> failureStartedAt = new AtomicReference<Instant>();

    public HalfOpenConnectionState(String hostName, int port) {
        super(hostName, port);
        failureStartedAt.set(Instant.now());
    }

    @Override
    public CacheService<K, V> access(IContext context) {
        var nextState =
                getRedisStatus().isUp() ? new ActiveConnectionState(hostName, port)
                        : getDowntime().compareTo(DowntimeConfig.downtime) > 0?
                        new FailedConnectionState(hostName, port):
                        null;
        goNext(context, nextState);
        return new NoCacheImpl<K, V>();
    }

    private Duration getDowntime(){
        return Duration.between(failureStartedAt.get(), Instant.now());
    }
}
