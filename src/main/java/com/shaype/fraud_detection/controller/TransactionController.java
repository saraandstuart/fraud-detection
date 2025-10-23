package com.shaype.fraud_detection.controller;

import com.shaype.fraud_detection.model.Transaction;
import com.shaype.fraud_detection.service.TransactionProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/transactions")
public class TransactionController {
    private final TransactionProducerService transactionProducerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTransaction(@RequestBody Transaction transaction) {
        transactionProducerService.publishMessage(transaction);
    }
}
