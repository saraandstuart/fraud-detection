package com.shaype.fraud_detection.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Table(name = "transaction")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "timestamp")
    private LocalDateTime timeStamp;

    public Transaction() {

    }

    public Transaction(
        String transactionId,
        String accountId,
        Double amount,
        Double latitude,
        Double longitude,
        LocalDateTime timeStamp
    ) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.amount = amount;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timeStamp = timeStamp;
    }
}
