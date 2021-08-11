package com.example.eventreceiver.endpoints.REST.controllers;

import com.example.eventreceiver.config.PubSubConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/")
public class HardcodeController {


  /*  private final DeviceDataSender deviceDataSender;

    public HardcodeController(DeviceDataSender deviceDataSender) {
        this.deviceDataSender = deviceDataSender;
    }

    @GetMapping("/hardcode")
    public void getData(){
        deviceDataSender.sendData("");
    }*/

    @Autowired
    private PubSubConfig.PubsubOutboundGateway messagingGateway;


    @PostMapping()
    public void postMessage(@RequestParam("message") String message) {
        this.messagingGateway.sendToPubsub(message);
    }
}
