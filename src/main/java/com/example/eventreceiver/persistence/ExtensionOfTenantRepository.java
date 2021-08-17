package com.example.eventreceiver.persistence;

public interface ExtensionOfTenantRepository {

    String findTenantIdByUrls(String urls);
}
