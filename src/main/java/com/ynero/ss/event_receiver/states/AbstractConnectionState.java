package com.ynero.ss.event_receiver.states;

import lombok.Builder;
import lombok.Data;
import redis.clients.jedis.Jedis;

public abstract class AbstractConnectionState<T> implements ConnectionState<T> {
    protected String hostName;
    protected int port;
    protected String info;

    public AbstractConnectionState(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }

    protected RedisStatus getRedisStatus() {
        try {
            var jedis = new Jedis(hostName, port);
            var info = jedis.info("server");
            jedis.close();
            return RedisStatus.builder()
                    .isUp(true)
                    .details(info)
                    .build();
        } catch (Exception ex) {
            return RedisStatus.builder()
                    .isUp(false)
                    .details(ex.getMessage())
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
}
