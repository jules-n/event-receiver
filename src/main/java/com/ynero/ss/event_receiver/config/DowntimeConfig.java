package com.ynero.ss.event_receiver.config;


import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class DowntimeConfig {

    public DowntimeConfig(@Value("${spring.data.redis.max-downtime}") Duration downtime) {
        this.downtime = downtime;
    }

    public static Duration downtime;
}
