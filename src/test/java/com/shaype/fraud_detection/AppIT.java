package com.shaype.fraud_detection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.shaype.fraud_detection.model.FraudAlert;
import com.shaype.fraud_detection.model.Transaction;
import io.restassured.RestAssured;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;

public class AppIT {

    private static final Double LATITUDE = 54.597286;
    private static final Double LONGITUDE = -5.930120;

    private final ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8089";
    }

    @Test
    public void triggerLargeTransactionRule() throws JsonProcessingException {
        for (int i = 0; i < 3; i++) {
            String transactionId = "LargeTransactionRuleTest" + i;
            String accountId = "LargeTransactionRuleTest" + i;
            Double amount = 9999.0 + i;
            LocalDateTime timestamp = LocalDateTime.now().plusMinutes(i);

            Transaction transaction = new Transaction(transactionId, accountId, amount, LATITUDE, LONGITUDE, timestamp);
            String bodyJson = objectMapper.writeValueAsString(transaction);

            given()
                .contentType("application/json")
                .body(bodyJson)
                .when()
                .post("/v1/transactions")
                .then().statusCode(201);
        }

        // then
        await().until(() -> {
                List<FraudAlert> fraudAlerts = given()
                    .when()
                    .get("/v1/fraud-alerts")
                    .then().statusCode(200)
                    .extract().body().jsonPath().getList(".", FraudAlert.class);

                return fraudAlerts.stream().anyMatch(t -> t.getRuleType().equals("LARGE_TRANSACTION_RULE"));
            }
        );
    }

    @Test
    public void triggerRapidBurstTransactionRule() throws JsonProcessingException {
        for (int i = 0; i < 5; i++) {
            String transactionId = "RapidTransactionBurstRuleTest" + i;
            String accountId = "accountId";
            Double amount = 100.0 + i;
            LocalDateTime timestamp = LocalDateTime.now().plusSeconds(i);

            Transaction transaction = new Transaction(transactionId, accountId, amount, LATITUDE, LONGITUDE, timestamp);
            String bodyJson = objectMapper.writeValueAsString(transaction);

            given()
                .contentType("application/json")
                .body(bodyJson)
                .when()
                .post("/v1/transactions")
                .then().statusCode(201);
        }

        // then
        await().until(() -> {
                List<FraudAlert> fraudAlerts = given()
                    .when()
                    .get("/v1/fraud-alerts")
                    .then().statusCode(200)
                    .extract().body().jsonPath().getList(".", FraudAlert.class);

                return fraudAlerts.stream().anyMatch(t -> t.getRuleType().equals("RAPID_TRANSACTION_BURST_RULE"));
            }
        );
    }

    @Test
    public void triggerGeographicImprobabilityRule() throws JsonProcessingException {
        List<Transaction> transactions = List.of(
            new Transaction("GeographicImprobabilityRuleTest1", "GeographicImprobabilityRuleAccountId", 100.0, 54.597286, -5.930120, LocalDateTime.now()),
            new Transaction("GeographicImprobabilityRuleTest2", "GeographicImprobabilityRuleAccountId", 100.0, 40.712776, -74.005974, LocalDateTime.now().plusMinutes(10))
        );

        for (Transaction transaction : transactions) {
            String bodyJson = objectMapper.writeValueAsString(transaction);

            given()
                .contentType("application/json")
                .body(bodyJson)
                .when()
                .post("/v1/transactions")
                .then().statusCode(201);
        }

        // then
        await().until(() -> {
                List<FraudAlert> fraudAlerts = given()
                    .when()
                    .get("/v1/fraud-alerts")
                    .then().statusCode(200)
                    .extract().body().jsonPath().getList(".", FraudAlert.class);

                return fraudAlerts.stream().anyMatch(t -> t.getRuleType().equals("GEOGRAPHIC_IMPROBABILITY_RULE"));
            }
        );
    }
}
