package com.ynero.ss.event_receiver.adapters;

import com.ynero.ss.event_receiver.domain.DeviceDTO;
import com.ynero.ss.event_receiver.domain.Event;
import com.ynero.ss.event_receiver.services.converters.DeviceToMessageJSONConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MapToStringMessageAdapter {

    @Autowired
    private DeviceToMessageJSONConverter<DeviceDTO> converter;

    public String getJSONDeviceDTO(Map<String, Object> messageMap) {
        var deviceId = UUID.fromString(messageMap.get("deviceId").toString());
        var eventsData = new HashMap<String, Object>();

        messageMap.keySet().forEach(
                key -> {
                    if (!key.equals("deviceId") && !key.equals("type")) {
                        eventsData.put(key, messageMap.get(key));
                    }
                }
        );

        Event event = Event.builder()
                .type(messageMap.get("type").toString())
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
