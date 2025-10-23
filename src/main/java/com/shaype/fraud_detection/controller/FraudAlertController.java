package com.shaype.fraud_detection.controller;

import com.shaype.fraud_detection.model.FraudAlert;
import com.shaype.fraud_detection.repository.FraudAlertRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/fraud-alerts")
public class FraudAlertController {

    private final FraudAlertRepository fraudAlertRepo;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FraudAlert> getFraudAlerts() {
        return fraudAlertRepo.findAll();
    }
}
