package com.ynero.ss.event_receiver.healthcheck;

import lombok.Builder;
import lombok.Data;
import redis.clients.jedis.Jedis;

public abstract class AbstractTolerableRedisConnState implements TolerableRedisHealthState {
    protected Jedis jedis;
    protected String jedisInfo;

    public AbstractTolerableRedisConnState(Jedis jedis) {
        this.jedis = jedis;
    }

    @Override
    public TolerableRedisHealthState onEnter(RedisHealthCheckOnFailure ctx) {
        jedisInfo = getJedisStatus().getDetails();
        return null;
    }

    protected JedisServerStatus getJedisStatus() {
        try {
            return JedisServerStatus.builder()
                    .isUp(true)
                    .details(jedis.info("server"))
                    .build();
        } catch (Exception ex) {
            return JedisServerStatus.builder()
                    .isUp(false)
                    .details("not available")
                    .build();
        }
    }

    @Data
    @Builder
    static class JedisServerStatus {
        private boolean isUp;
        private String details;
    }
}
