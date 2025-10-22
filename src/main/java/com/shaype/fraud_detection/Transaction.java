package com.shaype.fraud_detection;

import java.time.LocalDateTime;

public record Transaction(
    LocalDateTime timeStamp,
    String transactionId,
    String accountId,
    Double amount
) {
}
