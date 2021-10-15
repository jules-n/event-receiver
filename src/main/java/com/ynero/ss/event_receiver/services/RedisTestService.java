package com.ynero.ss.event_receiver.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * FIXME: remove this bean after redis-test experiment
 */
@Service
@Slf4j
public class RedisTestService {
    private final RedisTemplate<String, Object> redisTemplate;
    private ScheduledExecutorService exec;
    private ScheduledFuture activityJob;
    private final AtomicInteger redisActivityCounter = new AtomicInteger();

    public RedisTestService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    void startRedisActivity() {
        log.info("start redis activity");
        exec = Executors.newScheduledThreadPool(1);
        activityJob = exec.scheduleAtFixedRate(() -> {
            var testKey = "test-redis-activity-key";
            var value = "test-redis-activity-value";
            log.info("start redis activity operation: cnt={}", redisActivityCounter.get());
            redisTemplate.opsForValue().set(testKey, value, 600, TimeUnit.SECONDS);
            var retrievedValue = redisTemplate.opsForValue().get(testKey);
            log.info("finished redis activity operation: cnt={}", redisActivityCounter.get());
            redisActivityCounter.incrementAndGet();
        }, 5_000L, 1_000L, TimeUnit.MILLISECONDS);
    }

    @PreDestroy
    void stopRedisActivity() {
        activityJob.cancel(true);
        exec.shutdown();
    }
}
