package com.shaype.fraud_detection.rules;

import com.shaype.fraud_detection.model.FraudAlert;
import com.shaype.fraud_detection.model.Transaction;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LargeTransactionRuleTest {

    @Test
    public void shouldReturnFraudAlert_WhenTransactionIsLargerThanThreshold() {
        // given
        Instant fixedInstant = LocalDate.of(2025, 10, 22).atStartOfDay(ZoneOffset.UTC).toInstant();
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneOffset.UTC);

        Transaction transaction = new Transaction("1", "1", 10001.0, LocalDateTime.now(fixedClock));

        FraudRule sut = new LargeTransactionRule(fixedClock);

        // when
        FraudAlert actual = sut.evaluate(transaction, null);

        // then
        assertEquals("1", actual.getTransactionId());
        assertEquals("1", actual.getAccountId());
        assertEquals("LARGE_TRANSACTION_RULE", actual.getRuleType());
        assertEquals("Transaction amount 10001.0 exceeds threshold of 10000.0", actual.getDescription());
        assertEquals(fixedInstant, actual.getDetectionTime());
    }
}
