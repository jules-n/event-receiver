package com.example.eventreceiver.endpoints.REST.controllers;


import com.example.eventreceiver.repository.TenantRepository;
import com.example.eventreceiver.services.recognizer.PATH;
import com.example.eventreceiver.services.recognizer.TenantRecognizer;
import com.example.eventreceiver.services.sender.PubSubSender;
import com.google.common.collect.ImmutableMap;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

@RestController
@Log4j2
@RequestMapping("/messages")
public class ReceiverController {

    @Autowired
    private PubSubSender pubSubSender;

    @Autowired
    private TenantRecognizer tenantRecognizer;

    @PostMapping("/{tenant}")
    public ResponseEntity postMessage(@PathVariable String tenant, @RequestParam("message") String message) throws IOException, ExecutionException, InterruptedException {
        log.info(tenant);
        var answer = pubSubSender.sendToPubsub(message, tenantRecognizer.getTenant(new HashMap<>() {{
            put(PATH.URL, tenant);
        }}));
        log.info(answer);
        return ResponseEntity.ok().build();
    }
}
