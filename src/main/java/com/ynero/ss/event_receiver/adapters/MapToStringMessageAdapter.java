package com.ynero.ss.event_receiver.adapters;

import com.ynero.ss.event_receiver.persistence.TenantService;
import dtos.DeviceDTO;
import dtos.EventDTO;
import json_converters.DTOToMessageJSONConverter;
import dtos.PortDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class MapToStringMessageAdapter {

    @Autowired
    private DTOToMessageJSONConverter<DeviceDTO> converter;

    @Autowired
    private TenantService tenantService;

    public String adapt(Map<String, Object> messageMap, String tenantId) {

        var tenant = tenantService.findByTenantId(tenantId).get();
        var deviceData = tenant.getDeviceData();
        var deviceIdAlias = deviceData.getDeviceIdAlias();
        var eventTypeAlias = deviceData.getEventTypeAlias();
        var deviceId = UUID.fromString(messageMap.get(deviceIdAlias).toString());

        var eventBuilder = EventDTO.builder()
                .time(null);

        messageMap.keySet().forEach(
                key -> {
                    if (!key.equals(deviceIdAlias) && !key.equals(eventTypeAlias)) {
                        eventBuilder
                                .type(key)
                                .value(messageMap.get(key));
                    }
                }
        );

        var event = eventBuilder.build();

        PortDTO port = PortDTO.builder()
                .name(messageMap.get(eventTypeAlias).toString())
                .event(event)
                .build();

        var deviceDTO = DeviceDTO.builder()
                .id(deviceId)
                .port(port)
                .build();

        var convertedMessage = converter.serialize(deviceDTO);
        return convertedMessage;
    }
}
