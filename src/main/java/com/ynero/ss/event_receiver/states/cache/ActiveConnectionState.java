package com.ynero.ss.event_receiver.states.cache;

import com.ynero.ss.event_receiver.config.LettuceConfig;
import com.ynero.ss.event_receiver.states.AbstractConnectionState;
import com.ynero.ss.event_receiver.states.IContext;
import com.ynero.ss.event_receiver.states.healthcheck.HalfOpenConnectionState;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import services.CacheService;
import services.SimpleRedisCacheServiceImpl;

public class ActiveConnectionState<K, V> extends AbstractConnectionState<CacheService<K, V>> {

    public ActiveConnectionState(Jedis jedis) {
        super(jedis);
    }

    @Setter(onMethod_ = {@Autowired})
    private LettuceConfig lettuceConfig;

    @Override
    public CacheService<K, V> access(IContext context) {
        var nextState = getRedisStatus().isUp() ? null : new HalfOpenConnectionState(jedis);
        goNext(context, nextState);
        return new SimpleRedisCacheServiceImpl(lettuceConfig.redisTemplate());
    }
}