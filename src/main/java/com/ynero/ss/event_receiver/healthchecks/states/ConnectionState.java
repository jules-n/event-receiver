package com.ynero.ss.event_receiver.healthchecks.states;

import com.ynero.ss.event_receiver.healthchecks.TolerableRedisHealthCheck;
import jdk.jshell.spi.ExecutionControl;
import lombok.SneakyThrows;
import org.springframework.boot.actuate.health.Health;

public interface ConnectionState extends AutoCloseable{

    Health access(TolerableRedisHealthCheck context);

    ConnectionState next(TolerableRedisHealthCheck context);

    @SneakyThrows
    default ConnectionState onEnter(TolerableRedisHealthCheck context) {
        throw new ExecutionControl.NotImplementedException("Not implemented method onEnter");
    }
}
