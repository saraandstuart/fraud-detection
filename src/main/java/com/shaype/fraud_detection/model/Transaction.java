package com.shaype.fraud_detection.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "transaction")
@Data
@AllArgsConstructor
public class Transaction {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String transactionId;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "timestamp")
    private LocalDateTime timeStamp;

    public Transaction() {

    }
}
