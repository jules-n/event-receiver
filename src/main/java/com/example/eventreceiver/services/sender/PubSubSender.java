package com.example.eventreceiver.services.sender;

import com.google.api.core.ApiFuture;
import com.google.common.collect.ImmutableMap;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Configuration
@Log4j2
public class PubSubSender {

    @Value("${spring.cloud.stream.bindings.output.destination}")
    private String topic;

    @Value("${spring.cloud.gcp.project-id}")
    private String projectId;

    @Value("${tenant-id.attribute-key}")
    private String tenantId;

    public String sendToPubsub(String message, String tenantId) throws IOException, ExecutionException, InterruptedException {
        TopicName topicName = TopicName.of(projectId, topic);

        try (Publisher publisher = new Publisher(topicName)){
            ByteString data = ByteString.copyFromUtf8(message);
            PubsubMessage pubsubMessage = PubsubMessage
                    .newBuilder().setData(data)
                    .putAllAttributes(ImmutableMap.of(this.tenantId, tenantId))
                    .build();

            ApiFuture<String> messageIdFuture = publisher.getPublisher().publish(pubsubMessage);
            String messageId = messageIdFuture.get();
            return "Published message ID: " + messageId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Getter
    class Publisher implements AutoCloseable{

        private com.google.cloud.pubsub.v1.Publisher publisher;

        public Publisher(TopicName topic) throws IOException {
            publisher = com.google.cloud.pubsub.v1.Publisher.newBuilder(topic).build();
        }

        @Override
        public void close() throws Exception {
            publisher.shutdown();
            publisher.awaitTermination(1, TimeUnit.MINUTES);
        }
    }

}
