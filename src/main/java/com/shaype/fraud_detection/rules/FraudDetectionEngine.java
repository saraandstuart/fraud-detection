package com.shaype.fraud_detection.rules;

import com.shaype.fraud_detection.model.FraudAlert;
import com.shaype.fraud_detection.model.Transaction;
import com.shaype.fraud_detection.service.TransactionContextService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FraudDetectionEngine {

    private final List<FraudRule> rules;
    private final TransactionContextService contextService;

    @Autowired
    public FraudDetectionEngine(List<FraudRule> rules, TransactionContextService contextService) {
        this.rules = rules.stream()
            .sorted(Comparator.comparingInt(FraudRule::getPriority))
            .collect(Collectors.toList());
        this.contextService = contextService;
    }

    public List<FraudAlert> evaluate(Transaction transaction) {
        TransactionContext context = contextService.getTransactionContext(transaction);
        List<FraudAlert> alerts = new ArrayList<>();

        for (FraudRule rule : rules) {
            try {
                FraudAlert alert = rule.evaluate(transaction, context);
                if (alert != null) {
                    alerts.add(alert);
                }
            } catch (Exception e) {
                log.error("Error executing rule {}: {}", rule.getRuleName(), e.getMessage());
            }
        }

        return alerts;
    }
}
