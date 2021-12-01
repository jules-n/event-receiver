package com.ynero.ss.event_receiver.states.healthcheck;

import com.ynero.ss.event_receiver.config.DowntimeConfig;
import com.ynero.ss.event_receiver.states.AbstractConnectionState;
import com.ynero.ss.event_receiver.states.IContext;
import org.springframework.boot.actuate.health.Health;
import redis.clients.jedis.Jedis;


public class FailedConnectionState extends AbstractConnectionState<Health> {

    public FailedConnectionState(String hostName, int port) {
        super(hostName, port);
    }

    @Override
    public Health access(IContext context) {
        var nextState = getRedisStatus().isUp() ? new ActiveConnectionState(hostName, port) : null;
        goNext(context, nextState);
        return Health.down().withDetail("total downtime: ", DowntimeConfig.downtime).build();
    }
}
