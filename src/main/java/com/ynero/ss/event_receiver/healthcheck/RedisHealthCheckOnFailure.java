package com.ynero.ss.event_receiver.healthcheck;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration("redis")
@ConditionalOnProperty(name = {"redis.reestablishing"},
        havingValue = "true")
@Log4j2
public class RedisHealthCheckOnFailure implements HealthIndicator {

    @Setter(onMethod_ = @Value("${spring.data.redis.host}"))
    private String hostName;

    @Setter(onMethod_ = @Value("${spring.data.redis.port}"))
    private int port;

    @Setter(onMethod_ = @Value("${redis.attempts}"))
    private int attempts;

    private static int retry = 0;

    @Override
    public Health health() {
        try {
            Jedis jedis = new Jedis(hostName, port);
            var info = jedis.info("server");
            return Health.up().withDetail("info: ", info).build();
        } catch (Exception ex) {
            if (retry++ < attempts) {
                return Health.up().withDetail("countdown: ", attempts-retry).build();
            }
            return Health.down(ex).build();
        }
    }
}
