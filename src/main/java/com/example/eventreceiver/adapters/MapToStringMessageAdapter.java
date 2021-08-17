package com.example.eventreceiver.adapters;

import com.example.eventreceiver.domain.DeviceDTO;
import com.example.eventreceiver.domain.Event;
import com.example.eventreceiver.services.converters.DeviceToMessageJSONConverter;
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
