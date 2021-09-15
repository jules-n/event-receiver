package com.ynero.ss.event_receiver.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceData implements Serializable {
    private String deviceIdAlias;
    private String eventTypeAlias;
}
