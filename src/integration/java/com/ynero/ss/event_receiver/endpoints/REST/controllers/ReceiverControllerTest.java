package com.ynero.ss.event_receiver.endpoints.REST.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ynero.ss.event_receiver.IntegrationTestSetUp;
import com.ynero.ss.event_receiver.domain.DeviceData;
import com.ynero.ss.event_receiver.domain.Tenant;
import com.ynero.ss.event_receiver.persistence.TenantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
@SpringBootTest
@AutoConfigureDataMongo
@ActiveProfiles("integration-test")
@Testcontainers
@DirtiesContext
@AutoConfigureMockMvc
public class ReceiverControllerTest extends IntegrationTestSetUp {

    private Tenant tenant;
    private String tenantsUrl = "audioslave";
    private ObjectMapper mapper = new ObjectMapper();
    ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
    private Map<String, Object> message = Map.of(
    "deviceId", "8eb8dc69-5e74-4c4c-b05b-4c85af7a86ca",
    "type", "temperature-read",
    "temperature", Double.valueOf("27.5"));
    private String requestJson;
    private Set<String> urls = new LinkedHashSet<>();
    private Set<String> topics = new LinkedHashSet<>();

    @Autowired
    private TenantService tenantService;

    @BeforeEach
    void setUp() throws Exception {
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        requestJson = objectWriter.writeValueAsString(message);
        urls.add(tenantsUrl);
        topics.add("scorpions");
        var deviceData = new DeviceData("deviceId", "type");
        tenant = new Tenant("t-6", topics, urls, deviceData, null);
        tenantService.save(tenant);
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void receiveMessageShouldReturnOKWhenGetCorrectTenantUrlAndBodyMessage() throws Exception {
        this.mockMvc.perform(post("/messages/"+tenantsUrl)
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andDo(log())
                .andExpect(status().isOk());
    }

    @Test
    public void receiveMessageShouldReturn400WhenGetNonExistingTenantUrl() throws Exception {
        this.mockMvc.perform(post("/messages/Elysium")
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andDo(log())
                .andExpect(status().isNotFound());
    }
}
