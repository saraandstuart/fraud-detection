package com.shaype.fraud_detection.service;

import com.shaype.fraud_detection.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionProducerService {

    @Value("${app.topic-name}")
    private String topicName;

    private final KafkaTemplate<String, Transaction> kafkaTemplate;

    public void publishMessage(Transaction transaction) {
        log.info("Publishing message: {}", transaction);

        var future = kafkaTemplate.send(topicName, transaction.getAccountId(), transaction);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Message published successfully: metadata: {}, key: {}, value: {}",
                    result.getRecordMetadata(),
                    result.getProducerRecord().key(),
                    result.getProducerRecord().value()
                );
            } else {
                log.error("Error while publishing message", ex);
            }
        });
    }
}
