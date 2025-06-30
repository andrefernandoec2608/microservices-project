package com.andre.client.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class ClientEventProducer {

    @Value("${spring.kafka.topic}")
    private String topic;
    private final KafkaTemplate<Integer, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendEventMainAsyn(EventMain eventSent) throws JsonProcessingException {

        List<Header> recordHeaders = List.of(new RecordHeader("event-source", "scanner".getBytes()));
        Integer key = eventSent.clientId().intValue();
        String value = objectMapper.writeValueAsString(eventSent);
        ProducerRecord<Integer, String> producerRecord = new ProducerRecord<>(topic, null, key, value, recordHeaders);

        kafkaTemplate.send(producerRecord)
            .thenAccept(result -> {
                log.info(
                        "Message Sent Asyn Successfully for the Client Id: {} and the partition is {}",
                        eventSent.clientId(), result.getRecordMetadata().partition());
            })
            .exceptionally(ex -> {
                log.error("Error Sending the Message. Exception: {}", ex.getMessage(), ex);
                return null;
            });
    }
}
