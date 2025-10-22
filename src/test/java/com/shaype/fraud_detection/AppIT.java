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
import org.junit.jupiter.api.Disabled;
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

    @Disabled
    @Test
    public void loadTransactions() throws JsonProcessingException {
        Instant fixedInstant = LocalDate.of(2025, 10, 22).atStartOfDay(ZoneOffset.UTC).toInstant();
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneOffset.UTC);

        Transaction transaction = new Transaction("2", "2", 10001.0, LocalDateTime.now(fixedClock));
        String bodyJson = objectMapper.writeValueAsString(transaction);

        given()
            .contentType("application/json")
            .body(bodyJson)
            .when()
            .post("/v1/transactions")
            .then().statusCode(201);
    }
}
