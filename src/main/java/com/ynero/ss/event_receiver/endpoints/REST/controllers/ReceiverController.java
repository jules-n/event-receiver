package com.ynero.ss.event_receiver.endpoints.REST.controllers;


import com.ynero.ss.event_receiver.adapters.MapToStringMessageAdapter;
import com.ynero.ss.event_receiver.services.recognizer.message.MessageRecognizer;
import com.ynero.ss.event_receiver.services.recognizer.tenant.PATH;
import com.ynero.ss.event_receiver.services.recognizer.tenant.TenantRecognizer;
import com.ynero.ss.event_receiver.services.sender.PubSubSender;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

    @Autowired
    private MapToStringMessageAdapter adapter;

    @PostMapping("/{tenantsUrl}")
    public ResponseEntity postMessage(@PathVariable String tenantsUrl,
                                      @RequestParam(required = false) Object paramMessage,
                                      @RequestBody(required = false) Object bodyMessage,
                                      @RequestHeader(required = false) Object headerMessage)
            throws IOException, ExecutionException, InterruptedException {
        var message = messageRecognizer.getMessage(paramMessage, bodyMessage, headerMessage);
        var tenantId = tenantRecognizer.getTenant(new HashMap<>() {{
            put(PATH.URL, tenantsUrl);
        }});
        var deviceData = adapter.adapt((Map<String,Object>)message, tenantId);
        var answer = pubSubSender.sendToPubsub(deviceData, tenantId);
        log.info(answer);
        return ResponseEntity.ok().build();
    }
}
