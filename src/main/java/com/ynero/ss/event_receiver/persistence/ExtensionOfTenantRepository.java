package com.ynero.ss.event_receiver.persistence;

public interface ExtensionOfTenantRepository {

    String findTenantIdByUrls(String urls);
}
