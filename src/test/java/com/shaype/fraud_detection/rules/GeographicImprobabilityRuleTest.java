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

public class GeographicImprobabilityRuleTest {

    @Test
    public void shouldReturnFraudAlert_WhenTransactionsAreImplausiblyDistantFromEachOther() {
        // given
        Instant fixedInstant = LocalDate.of(2025, 10, 22).atStartOfDay(ZoneOffset.UTC).toInstant();
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneOffset.UTC);

        List<Transaction> recentTransactions = new ArrayList<>();
        recentTransactions.add(new Transaction("1", "accountId", 100.0, 54.597286, -5.930120, LocalDateTime.now(fixedClock))); // Belfast

        TransactionContext context = new TransactionContext(recentTransactions);

        Transaction transaction = new Transaction("2", "accountId", 100.0, 40.712776, -74.005974, LocalDateTime.now(fixedClock).plusMinutes(10)); // New York

        FraudRule sut = new GeographicImprobabilityRule(fixedClock);

        // when
        FraudAlert actual = sut.evaluate(transaction, context);

        // then
        assertEquals("2", actual.getTransactionId());
        assertEquals("accountId", actual.getAccountId());
        assertEquals("GEOGRAPHIC_IMPROBABILITY_RULE", actual.getRuleType());
        assertEquals("Impossible travel: 5106 km in 10 minutes (30639 km/h required)", actual.getDescription());
        assertEquals(fixedInstant, actual.getDetectionTime());
    }
}
