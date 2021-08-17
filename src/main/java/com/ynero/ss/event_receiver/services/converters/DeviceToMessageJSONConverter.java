package com.ynero.ss.event_receiver.services.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceToMessageJSONConverter<T>{

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    public <T> T deserialize(String msg, Class<T> dtoType) {
        return objectMapper.readValue(msg, dtoType);
    }

    @SneakyThrows
    public String serialize(T object) {
        return objectMapper.writeValueAsString(object);
    }
}
