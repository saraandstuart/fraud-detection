package com.shaype.fraud_detection.repository;

import com.shaype.fraud_detection.model.Transaction;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findByAccountIdAndTimeStampAfter(String accountId, LocalDateTime cutoff);
}
