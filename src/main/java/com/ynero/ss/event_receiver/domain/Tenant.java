package com.ynero.ss.event_receiver.domain;

import com.mongodb.lang.Nullable;
import domain.SendingParameters;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = Tenant.COLLECTION_NAME)
public class Tenant{
    public static final String COLLECTION_NAME = "tenants";
    @Indexed(unique = true, name = "tenantId_")
    @Field(name = "tenantId")
    private String tenantId;
    private Set<String> topics;
    private Set<String> urls;
    @NonNull
    private DeviceData deviceData;
    @Nullable
    private SendingParameters parameters;

}
