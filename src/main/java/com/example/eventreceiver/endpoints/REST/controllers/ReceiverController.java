package com.example.eventreceiver.endpoints.REST.controllers;


import com.example.eventreceiver.services.recognizer.message.MessageRecognizer;
import com.example.eventreceiver.services.recognizer.tenant.PATH;
import com.example.eventreceiver.services.recognizer.tenant.TenantRecognizer;
import com.example.eventreceiver.services.sender.PubSubSender;
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

    @Autowired
    private MessageRecognizer messageRecognizer;

    @PostMapping("/{tenantsUrl}")
    public ResponseEntity postMessage(@PathVariable String tenantsUrl,
                                      @RequestParam(required = false) Object paramMessage,
                                      @RequestBody(required = false) Object bodyMessage,
                                      @RequestHeader(required = false) Object headerMessage)
            throws IOException, ExecutionException, InterruptedException {
        var message = messageRecognizer.getMessage(paramMessage, bodyMessage, headerMessage);
        var answer = pubSubSender.sendToPubsub(message, tenantRecognizer.getTenant(new HashMap<>() {{
            put(PATH.URL, tenantsUrl);
        }}));
        log.info(answer);
        return ResponseEntity.ok().build();
    }
}