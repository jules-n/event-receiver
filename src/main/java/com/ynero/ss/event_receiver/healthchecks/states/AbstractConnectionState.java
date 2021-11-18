package com.ynero.ss.event_receiver.healthchecks.states;

import com.ynero.ss.event_receiver.healthchecks.TolerableRedisHealthCheck;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import redis.clients.jedis.Jedis;

public abstract class AbstractConnectionState implements ConnectionState {
    protected Jedis jedis;
    protected String info;

    public AbstractConnectionState(Jedis jedis) {
        this.jedis = jedis;
    }

    protected RedisStatus getRedisStatus() {
        try {
            var info = jedis.info("server");
            return RedisStatus.builder()
                    .isUp(true)
                    .details(info)
                    .build();
        } catch (Exception ex) {
            return RedisStatus.builder()
                    .isUp(false)
                    .details("Could not connect to Redis")
                    .build();
        }
    }

    @Override
    @SneakyThrows
    public ConnectionState onEnter(TolerableRedisHealthCheck context) {
        info = getRedisStatus().getDetails();
        var nextState = context.getState().next(context);
        if (nextState != null) {
            context.setState(nextState);
            nextState.onEnter(context);
        }
        return nextState;
    }

    @Data
    @Builder
    public static class RedisStatus{
        private boolean isUp;
        private String details;
    }

    @Override
    public void close() throws Exception {
        jedis.close();
    }
}
