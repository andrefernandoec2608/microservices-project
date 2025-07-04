package com.andre.account.consumer;

import com.andre.account.service.ConsumerEventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AccountEventConsumer {

    private final ConsumerEventService consumerService;

    @KafkaListener(topics = "quito-events", groupId = "events-listener-group")
    public void onMessage(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        System.out.println("XXXXXXX" + consumerRecord);
        consumerService.processMainEvent(consumerRecord);
        log.info("ConsumerRecord: {} ", consumerRecord);
    }
}
