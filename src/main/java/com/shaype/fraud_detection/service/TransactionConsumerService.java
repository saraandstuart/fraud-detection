package com.shaype.fraud_detection.service;

import com.shaype.fraud_detection.model.FraudAlert;
import com.shaype.fraud_detection.model.Transaction;
import com.shaype.fraud_detection.repository.FraudAlertRepository;
import com.shaype.fraud_detection.repository.TransactionRepository;
import com.shaype.fraud_detection.rules.FraudDetectionEngine;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionConsumerService {

    private final FraudDetectionEngine fraudEngine;
    private final TransactionRepository transactionRepo;
    private final FraudAlertRepository fraudAlertRepo;

    @KafkaListener(topics = "${app.topic-name}", groupId = "${spring.kafka.consumer.group-id}", concurrency = "3")
    @Transactional
    public void consume(Transaction transaction) {
        log.info("Consuming message: {}", transaction);

        try {
            transactionRepo.save(transaction);
            log.debug("Saved transaction {} to database", transaction.getTransactionId());

            List<FraudAlert> alerts = fraudEngine.evaluate(transaction);

            if (!alerts.isEmpty()) {
                log.warn("Fraud alerts detected for transaction {}: {}", transaction.getTransactionId(), alerts.size());

                for (FraudAlert alert : alerts) {
                    handleFraudAlert(alert);
                }
            } else {
                log.debug("Transaction {} passed all fraud checks", transaction.getTransactionId());
            }
        } catch (Exception e) {
            log.error("Error processing transaction {}: {}", transaction.getTransactionId(), e.getMessage(), e);
            throw e;
        }
    }


    private void handleFraudAlert(FraudAlert alert) {
        log.info("Fraud Alert detected: {}", alert);
        fraudAlertRepo.save(alert);
        // potentially publish on a Kafka fraud alerts topic
    }
}
