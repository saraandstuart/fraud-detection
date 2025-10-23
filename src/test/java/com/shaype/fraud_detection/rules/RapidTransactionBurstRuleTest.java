package com.shaype.fraud_detection.rules;

import com.shaype.fraud_detection.model.FraudAlert;
import com.shaype.fraud_detection.model.Transaction;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RapidTransactionBurstRuleTest {

    private static final Double LATITUDE = 54.597286;
    private static final Double LONGITUDE = -5.930120;

    @Test
    public void shouldReturnFraudAlert_WhenMoreThanFiveTransactionsPerMinute() {
        // given
        Instant fixedInstant = LocalDate.of(2025, 10, 22).atStartOfDay(ZoneOffset.UTC).toInstant();
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneOffset.UTC);

        List<Transaction> recentTransactions = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            String transactionId = String.valueOf(i);
            Double amount = 100.0 + i;
            LocalDateTime timestamp = LocalDateTime.now(fixedClock).plusSeconds(i);

            Transaction transaction = new Transaction(transactionId, "accountId", amount, LATITUDE, LONGITUDE, timestamp);
            recentTransactions.add(transaction);
        }

        TransactionContext context = new TransactionContext(recentTransactions);

        Transaction transaction = new Transaction("6", "accountId", 106.0, LATITUDE, LONGITUDE, LocalDateTime.now(fixedClock).plusSeconds(6));

        FraudRule sut = new RapidTransactionBurstRule(fixedClock);

        // when
        FraudAlert actual = sut.evaluate(transaction, context);

        // then
        assertEquals("6", actual.getTransactionId());
        assertEquals("accountId", actual.getAccountId());
        assertEquals("RAPID_TRANSACTION_BURST_RULE", actual.getRuleType());
        assertEquals("6 transactions detected in 60 seconds", actual.getDescription());
        assertEquals(fixedInstant, actual.getDetectionTime());
    }
}
