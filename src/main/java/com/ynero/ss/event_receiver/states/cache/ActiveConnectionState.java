package com.ynero.ss.event_receiver.states.cache;

import com.ynero.ss.event_receiver.config.LettuceConfig;
import com.ynero.ss.event_receiver.states.AbstractConnectionState;
import com.ynero.ss.event_receiver.states.IContext;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import services.CacheService;

public class ActiveConnectionState<K, V> extends AbstractConnectionState<CacheService<K, V>> {

    @Setter(onMethod_ = {@Autowired})
    private LettuceConfig config;

    public ActiveConnectionState(String hostName, int port) {
        super(hostName, port);
        config = new LettuceConfig(hostName, port);
    }

    @Override
    public CacheService<K, V> access(IContext context) {
        var nextState = getRedisStatus().isUp() ? null : new HalfOpenConnectionState(hostName, port);
        goNext(context, nextState);
        return config.defaultCacheService();
    }
}