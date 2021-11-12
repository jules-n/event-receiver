package com.ynero.ss.event_receiver.adapters;

import com.ynero.ss.event_receiver.IntegrationTestSetUp;
import com.ynero.ss.event_receiver.domain.DeviceData;
import com.ynero.ss.event_receiver.domain.Tenant;
import com.ynero.ss.event_receiver.persistence.TenantService;
import dtos.DeviceDTO;
import dtos.EventDTO;
import dtos.PortDTO;
import json_converters.DTOToMessageJSONConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureDataMongo
@ActiveProfiles("integration-test")
@Testcontainers
@DirtiesContext
@AutoConfigureMockMvc
public class MapToStringMessageAdapterTest extends IntegrationTestSetUp {

    @Autowired
    private MapToStringMessageAdapter adapter;

    @Autowired
    private DTOToMessageJSONConverter<DeviceDTO> converter;

    @Autowired
    private TenantService tenantService;

    private Map<String, Object> message = Map.of(
            "deviceId", "8eb8dc69-5e74-4c4c-b05b-4c85af7a86ca",
            "type", "temperature-read",
            "temperature", Double.valueOf("27.5"));

    private Tenant tenant;

    @BeforeEach
    void setUp() throws Exception {
        var deviceData = new DeviceData("deviceId", "type");
        Set<String> urls = new LinkedHashSet<>();
        Set<String> topics = new LinkedHashSet<>();
        urls.add("audioslave");
        topics.add("scorpions");
        tenant = new Tenant("t-6",
                topics,
                urls,
                deviceData, null);
        tenantService.save(tenant);
    }

    @Test
    void findByTenantExistingIdShouldReturnCorrectStringWhenTenantExists() {
        var result = adapter.adapt(message, "t-6");
        var data = DeviceDTO.builder()
                .id(UUID.fromString("8eb8dc69-5e74-4c4c-b05b-4c85af7a86ca"))
                .port(
                        PortDTO.builder()
                                .event(EventDTO.builder()
                                        .time(null)
                                        .type("temperature")
                                        .value(27.5)
                                        .build())
                                .name("temperature-read")
                                .build()
                )
                .build();
        var expected = converter.serialize(data);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void findByTenantExistingIdShouldReturnExceptionWhenTenantDoesntExistsInDB() {
        var nonExistingTenantId = "Doctor Who";
        var expectedMessage = "No such tenantId: " + nonExistingTenantId;
        var result = assertThrows(NoSuchElementException.class, ()-> adapter.adapt(message, nonExistingTenantId)).getMessage();
        assertThat(result).isEqualTo(expectedMessage);
    }

}
