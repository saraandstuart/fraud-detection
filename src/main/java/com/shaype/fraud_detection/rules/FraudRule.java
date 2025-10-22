package com.shaype.fraud_detection.rules;

import com.shaype.fraud_detection.model.FraudAlert;
import com.shaype.fraud_detection.model.Transaction;

public interface FraudRule {
    String getRuleName();

    FraudAlert evaluate(Transaction transaction, TransactionContext context);

    int getPriority();
}
