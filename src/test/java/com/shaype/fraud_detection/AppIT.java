package com.shaype.fraud_detection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.shaype.fraud_detection.model.Transaction;
import io.restassured.RestAssured;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
        Instant fixedInstant = LocalDate.of(2025, 10, 22).atStartOfDay(ZoneOffset.UTC).toInstant();
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneOffset.UTC);

        for (int i = 0; i < 10; i++) {

            String transactionId = String.valueOf(i + 1);
            String accountId = String.valueOf(i + 1);
            Double amount = 9995.0 + i;
            LocalDateTime timestamp = LocalDateTime.now(fixedClock).plusMinutes(i);

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
