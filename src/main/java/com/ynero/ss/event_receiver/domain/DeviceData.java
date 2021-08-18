package com.ynero.ss.event_receiver.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceData {
    private String deviceIdAlias;
    private String eventTypeAlias;
}
