package com.example.eventreceiver.persistence;

public interface ExtensionOfTenantRepository {

    String findTenantIdByUrl(String url);
}
