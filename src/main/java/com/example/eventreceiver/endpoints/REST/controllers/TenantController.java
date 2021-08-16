package com.example.eventreceiver.endpoints.REST.controllers;

import com.example.eventreceiver.domain.Tenant;
import com.example.eventreceiver.persistence.TenantService;
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

    @PostMapping
    private ResponseEntity createTenant(@RequestBody Tenant tenant) throws Exception {
        tenantRepository.save(tenant);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping
    private ResponseEntity updateTenant(@RequestBody Tenant tenant) {
        var fountTenant = tenantRepository.findByTenantId(tenant.getTenantId());
        if(fountTenant.isEmpty()) return new ResponseEntity(HttpStatus.BAD_REQUEST);

            tenantRepository.update(tenant);


        return new ResponseEntity(HttpStatus.OK);
    }
}