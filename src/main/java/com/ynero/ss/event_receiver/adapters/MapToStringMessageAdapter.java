package com.ynero.ss.event_receiver.adapters;

import com.ynero.ss.event_receiver.persistence.TenantService;
import com.ynero.ss.event_receiver.services.converters.DeviceToMessageJSONConverter;
import dtos.DeviceDTO;
import dtos.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MapToStringMessageAdapter {

    @Autowired
    private DeviceToMessageJSONConverter<DeviceDTO> converter;

    @Autowired
    private TenantService tenantService;

    public String getJSONDeviceDTO(Map<String, Object> messageMap, String tenantId) {

        var tenant = tenantService.findByTenantId(tenantId).get();
        var deviceData = tenant.getDeviceData();
        var deviceIdAlias = deviceData.getDeviceIdAlias();
        var eventTypeAlias = deviceData.getEventTypeAlias();
        var deviceId = UUID.fromString(messageMap.get(deviceIdAlias).toString());
        var eventsData = new HashMap<String, Object>();

        messageMap.keySet().forEach(
                key -> {
                    if (!key.equals(deviceIdAlias) && !key.equals(eventTypeAlias)) {
                        eventsData.put(key, messageMap.get(key));
                    }
                }
        );

        Event event = Event.builder()
                .type(messageMap.get(eventTypeAlias).toString())
                .data(eventsData)
                .build();

        var deviceDTO = DeviceDTO.builder()
                .id(deviceId)
                .event(event)
                .build();

        var convertedMessage = converter.serialize(deviceDTO);
        return convertedMessage;
    }
}
