package com.ynero.ss.event_receiver.healthcheck;

import org.springframework.boot.actuate.health.Health;

public interface TolerableRedisHealthState {

    Health accessHealth(RedisHealthCheckOnFailure ctx);

    TolerableRedisHealthState nextState(RedisHealthCheckOnFailure ctx);

    // these are optional
    default TolerableRedisHealthState onEnter(RedisHealthCheckOnFailure ctx) {
        // or return this;
        return null;
    }

    default TolerableRedisHealthState onExit(RedisHealthCheckOnFailure ctx) {
        return null;
    }
}
