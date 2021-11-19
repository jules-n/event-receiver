package com.ynero.ss.event_receiver.states;

import lombok.Builder;
import lombok.Data;
import redis.clients.jedis.Jedis;

public abstract class AbstractConnectionState<T> implements ConnectionState<T> {
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
    public void onEnter(IContext context) {
        info = getRedisStatus().getDetails();
    }

    protected void goNext(IContext context, ConnectionState nextState) {
        if (nextState!=null) {
            context.setState(nextState);
            nextState.onEnter(context);
        }
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
