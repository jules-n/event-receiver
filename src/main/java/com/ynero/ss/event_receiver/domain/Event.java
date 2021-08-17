package com.ynero.ss.event_receiver.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Event {
    private String type;
    private Map<String,Object> data;
    private LocalDateTime time;
}
