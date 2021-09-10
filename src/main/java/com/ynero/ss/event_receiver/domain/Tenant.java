package com.ynero.ss.event_receiver.domain;

import com.mongodb.lang.Nullable;
import domain.SendingParameters;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = Tenant.COLLECTION_NAME)
public class Tenant {
    public static final String COLLECTION_NAME = "tenants";
    private String tenantId;
    private String[] topics;
    private String[] urls;
    @NonNull
    private DeviceData deviceData;
    @Nullable
    private SendingParameters parameters;

}
