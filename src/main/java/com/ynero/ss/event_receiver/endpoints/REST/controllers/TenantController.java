package com.ynero.ss.event_receiver.endpoints.REST.controllers;

import com.ynero.ss.event_receiver.domain.Tenant;
import com.ynero.ss.event_receiver.persistence.TenantService;
import com.ynero.ss.event_receiver.services.sender.KafkaSender;
import com.ynero.ss.event_receiver.services.sender.TenantsSendingDataSender;
import domain.TenantSendingData;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/tenants")
public class TenantController {

    @Autowired
    private TenantService tenantRepository;

    @Autowired
    private TenantsSendingDataSender sender;

    @PostMapping
    private ResponseEntity createTenant(@RequestBody Tenant tenant) throws Exception {
        var result = tenantRepository.save(tenant);
        sender.send(tenant);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping
    private ResponseEntity updateTenant(@RequestBody Tenant tenant) {
        var fountTenant = tenantRepository.findByTenantId(tenant.getTenantId());
        if (!fountTenant.isEmpty()) {
            var result = tenantRepository.update(tenant);
            if (result) {
                sender.send(tenant);
                return new ResponseEntity(HttpStatus.OK);
            }
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}