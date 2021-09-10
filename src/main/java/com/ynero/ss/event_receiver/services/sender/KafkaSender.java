package com.ynero.ss.event_receiver.services.sender;

import json_converters.DTOToMessageJSONConverter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaSender<T> {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final DTOToMessageJSONConverter converter;

    public KafkaSender(KafkaTemplate<String, String> kafkaTemplate, DTOToMessageJSONConverter converter) {
        this.kafkaTemplate = kafkaTemplate;
        this.converter = converter;
    }

    public void produce(T dto, String topic) {
        var message = converter.serialize(dto);
        kafkaTemplate.send(topic, message);
        kafkaTemplate.flush();
        kafkaTemplate.destroy();
    }
}
