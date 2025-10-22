package com.shaype.fraud_detection.rules;

import com.shaype.fraud_detection.model.FraudAlert;
import com.shaype.fraud_detection.model.Transaction;
import java.time.Clock;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LargeTransactionRule implements FraudRule {

    private static final double LARGE_TRANSACTION_AMOUNT = 10000.0;
    private final Clock clock;

    @Override
    public String getRuleName() {
        return "LARGE_TRANSACTION_RULE";
    }

    @Override
    public FraudAlert evaluate(Transaction transaction, TransactionContext context) {
        if (transaction.getAmount() > LARGE_TRANSACTION_AMOUNT) {
            return new FraudAlert(
                transaction.getTransactionId(),
                transaction.getAccountId(),
                getRuleName(),
                String.format("Transaction amount %s exceeds threshold of %s", transaction.getAmount(), LARGE_TRANSACTION_AMOUNT),
                Instant.now(clock)
            );
        }

        return null;
    }

    @Override
    public int getPriority() {
        return 1;
    }
}
