package com.shaype.fraud_detection.service;

import com.shaype.fraud_detection.model.Transaction;
import com.shaype.fraud_detection.repository.TransactionRepository;
import com.shaype.fraud_detection.rules.TransactionContext;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TransactionContextService {

    private final TransactionRepository transactionRepo;
    private final Clock clock;
    private final Duration timeWindow;

    @Autowired
    public TransactionContextService(TransactionRepository transactionRepo, Clock clock) {
        this(transactionRepo, clock, Duration.ofHours(1));
    }

    TransactionContextService(TransactionRepository transactionRepo, Clock clock, Duration timeWindow) {
        this.transactionRepo = transactionRepo;
        this.clock = clock;
        this.timeWindow = timeWindow;
    }

    public TransactionContext getTransactionContext(Transaction transaction) {
        List<Transaction> recentTransactions = getRecentTransactions(transaction.getAccountId(), timeWindow);
        return new TransactionContext(recentTransactions);
    }

    private List<Transaction> getRecentTransactions(String accountId, Duration window) {
        LocalDateTime cutoff = LocalDateTime.now(clock).minus(window);
        return transactionRepo.findByAccountIdAndTimeStampAfter(accountId, cutoff);
    }
}
