package com.ynero.ss.event_receiver.states.cache;

import com.ynero.ss.event_receiver.states.AbstractConnectionState;
import com.ynero.ss.event_receiver.states.IContext;
import com.ynero.ss.event_receiver.states.healthcheck.ActiveConnectionState;
import redis.clients.jedis.Jedis;
import services.CacheService;

public class FailedConnectionState <K, V> extends AbstractConnectionState<CacheService<K,V>> {

    public FailedConnectionState(Jedis jedis) {
        super(jedis);
    }

    @Override
    public CacheService<K, V> access(IContext context) {
        var nextState = getRedisStatus().isUp() ? new ActiveConnectionState(jedis) : null;
        goNext(context, nextState);
        throw new RuntimeException("Redis failed");
    }
}
