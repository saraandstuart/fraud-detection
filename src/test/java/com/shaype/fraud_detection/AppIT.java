package com.shaype.fraud_detection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.shaype.fraud_detection.model.Transaction;
import io.restassured.RestAssured;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class AppIT {

    private final ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8089";
    }

    @Test
    public void loadTransactions() throws JsonProcessingException {
        // trigger large transaction rule
        for (int i = 0; i < 3; i++) {
            String transactionId = "LargeTransactionRuleTest" + i;
            String accountId = "LargeTransactionRuleTest" + i;
            Double amount = 9999.0 + i;
            LocalDateTime timestamp = LocalDateTime.now().plusMinutes(i);

            Transaction transaction = new Transaction(transactionId, accountId, amount, timestamp);
            String bodyJson = objectMapper.writeValueAsString(transaction);

            given()
                .contentType("application/json")
                .body(bodyJson)
                .when()
                .post("/v1/transactions")
                .then().statusCode(201);
        }

        // trigger rapid burst transaction rule
        for (int i = 0; i < 5; i++) {
            String transactionId = "RapidTransactionBurstRuleTest" + (i + 1);
            String accountId = "accountId";
            Double amount = 100.0 + i;
            LocalDateTime timestamp = LocalDateTime.now().plusSeconds(i);

            Transaction transaction = new Transaction(transactionId, accountId, amount, timestamp);
            String bodyJson = objectMapper.writeValueAsString(transaction);

            given()
                .contentType("application/json")
                .body(bodyJson)
                .when()
                .post("/v1/transactions")
                .then().statusCode(201);
        }
    }
}
