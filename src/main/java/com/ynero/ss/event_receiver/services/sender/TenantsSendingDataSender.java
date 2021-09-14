package com.ynero.ss.event_receiver.services.sender;

import com.ynero.ss.event_receiver.domain.Tenant;
import domain.TenantSendingDataDTO;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TenantsSendingDataSender {

    @Setter(onMethod_ = {@Autowired})
    private KafkaSender<TenantSendingDataDTO> sender;

    @Setter(onMethod_ = {@Value("${spring.producer.tenants-sending-data-topic}")})
    private String topic;

    public void send(Tenant tenant) {
        var sendingParameters = tenant.getParameters();
        if (sendingParameters == null) return;
        var tenantSendingData = TenantSendingDataDTO.builder()
                .tenantId(tenant.getTenantId())
                .parameters(sendingParameters);
        sender.produce(tenantSendingData.build(), topic);
    }
}
