package com.shaype.fraud_detection.rules;

import com.shaype.fraud_detection.model.FraudAlert;
import com.shaype.fraud_detection.model.Transaction;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RapidTransactionBurstRule implements FraudRule {

    private static final int MAX_TRANSACTIONS_PER_MINUTE = 5;
    private static final Duration TIME_WINDOW = Duration.ofMinutes(1);
    private final Clock clock;

    @Override
    public String getRuleName() {
        return "RAPID_TRANSACTION_BURST_RULE";
    }

    @Override
    public FraudAlert evaluate(Transaction transaction, TransactionContext context) {
        Instant cutoff = transaction.getTimeStamp().toInstant(ZoneOffset.UTC).minus(TIME_WINDOW);

        long recentCount = context.recentTransactions().stream()
            .filter(t -> t.getTimeStamp().toInstant(ZoneOffset.UTC).isAfter(cutoff))
            .filter(t -> t.getAccountId().equals(transaction.getAccountId()))
            .count();

        if (recentCount >= MAX_TRANSACTIONS_PER_MINUTE) {
            return new FraudAlert(
                transaction.getTransactionId(),
                transaction.getAccountId(),
                getRuleName(),
                String.format(String.format("%d transactions detected in %d seconds", recentCount + 1, TIME_WINDOW.getSeconds())),
                Instant.now(clock)
            );
        }
        return null;
    }

    @Override
    public int getPriority() {
        return 2;
    }
}
