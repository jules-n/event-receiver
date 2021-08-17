package com.ynero.ss.event_receiver.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = Tenant.COLLECTION_NAME)
@ToString
public class Tenant {
    public static final String COLLECTION_NAME = "tenants";
    private String tenantId;
    private String[] topics;
    private String[] urls;
}
