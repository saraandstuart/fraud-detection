package com.shaype.fraud_detection.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Data;

@Entity
@Table(name = "fraud_alert")
@Data
public class FraudAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "transaction_id")
    String transactionId;

    @Column(name = "account_id")
    String accountId;

    @Column(name = "ruleType")
    String ruleType;

    @Column(name = "description")
    String description;

    @Column(name = "detection_time")
    Instant detectionTime;

    public FraudAlert() {

    }

    public FraudAlert(
        String transactionId,
        String accountId,
        String ruleType,
        String description,
        Instant detectionTime
    ) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.ruleType = ruleType;
        this.description = description;
        this.detectionTime = detectionTime;
    }

}
