package com.shaype.fraud_detection.repository;

import com.shaype.fraud_detection.model.FraudAlert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FraudAlertRepository extends JpaRepository<FraudAlert, Long> {
}
