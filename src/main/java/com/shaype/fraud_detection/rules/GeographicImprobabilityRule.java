package com.shaype.fraud_detection.rules;

import com.shaype.fraud_detection.model.FraudAlert;
import com.shaype.fraud_detection.model.Transaction;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GeographicImprobabilityRule implements FraudRule {

    private static final double EARTH_RADIUS_IN_KM = 6371;
    private static final double MAX_KM_PER_HOUR = 1000.0; // Airplane speed
    private final Clock clock;

    @Override
    public String getRuleName() {
        return "GEOGRAPHIC_IMPROBABILITY_RULE";
    }

    @Override
    public FraudAlert evaluate(Transaction transaction, TransactionContext context) {
        List<Transaction> recent = context.recentTransactions();
        if (recent.isEmpty()) {
            return null;
        }

        Transaction lastTransaction = recent.getFirst();
        double distance = calculateDistance(lastTransaction, transaction);

        long timeDiffMinutes = Duration.between(lastTransaction.getTimeStamp(), transaction.getTimeStamp()).toMinutes();

        double requiredSpeedKmh = (distance / timeDiffMinutes) * 60;

        if (requiredSpeedKmh > MAX_KM_PER_HOUR) {
            return new FraudAlert(
                transaction.getTransactionId(),
                transaction.getAccountId(),
                getRuleName(),
                String.format("Impossible travel: %.0f km in %d minutes (%.0f km/h required)", distance, timeDiffMinutes, requiredSpeedKmh),
                Instant.now(clock)
            );
        }
        return null;
    }

    private double calculateDistance(Transaction loc1, Transaction loc2) {
        // Haversine formula for distance calculation
        double lat1 = Math.toRadians(loc1.getLatitude());
        double lat2 = Math.toRadians(loc2.getLatitude());
        double dLat = lat2 - lat1;
        double dLon = Math.toRadians(loc2.getLongitude() - loc1.getLongitude());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(lat1) * Math.cos(lat2) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_IN_KM * c;
    }

    @Override
    public int getPriority() {
        return 3;
    }
}
