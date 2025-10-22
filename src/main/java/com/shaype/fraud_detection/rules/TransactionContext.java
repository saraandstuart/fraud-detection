package com.shaype.fraud_detection.rules;

import com.shaype.fraud_detection.model.Transaction;
import java.util.List;

public record TransactionContext(List<Transaction> recentTransactions) {
}
