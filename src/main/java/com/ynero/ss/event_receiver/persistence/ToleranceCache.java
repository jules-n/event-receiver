package com.ynero.ss.event_receiver.persistence;

import com.ynero.ss.event_receiver.states.ConnectionState;
import com.ynero.ss.event_receiver.states.IContext;
import com.ynero.ss.event_receiver.states.cache.HalfOpenConnectionState;
import lombok.Getter;
import lombok.Setter;
import models.Entry;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.Jedis;
import services.CacheService;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class ToleranceCache<K, V> implements CacheService<K, V>, IContext <CacheService<K, V>>{

    @Getter
    @Setter
    private ConnectionState<CacheService<K, V>> state;

    public ToleranceCache(@Value("${spring.data.redis.host}") String hostName, @Value("${spring.data.redis.port}") int port) {
        state = new HalfOpenConnectionState<K, V>(hostName, port);
    }

    @Override
    public void save(K k, V v, Integer integer, TimeUnit timeUnit) {
        state.access(this).save(k, v, integer, timeUnit);
    }

    @Override
    public void save(K k, V v) {
        state.access(this).save(k, v);
    }

    @Override
    public Optional<V> get(K k) {
        return state.access(this).get(k);
    }

    @Override
    public boolean delete(K k) {
        return state.access(this).delete(k);
    }

    @Override
    public boolean deleteMany(K... ks) {
        return state.access(this).deleteMany(ks);
    }

    @Override
    public void saveMany(List<Entry<K, V>> list, Integer integer, TimeUnit timeUnit) {
        state.access(this).saveMany(list, integer, timeUnit);
    }

    @Override
    public void saveMany(List<Entry<K, V>> list) {
        state.access(this).saveMany(list);
    }
}
