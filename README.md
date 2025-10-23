# Real-Time Fraud Detection Engine

## Build, run and stop the app
```shell
# Build app (also runs unit tests)
mvn clean install

# Build containers
docker compose build

# Run containers
docker compose up -d --wait

# Run integration tests
mvn verify -P integration-test

# stop containers
docker compose down
````

How to Publish a transaction message to Kafka via an HTTP endpoint e.g. using curl
```shell
curl 'http://localhost:8089/v1/transactions' -H 'Content-Type: application/json' --data '{"timeStamp": "2025-10-21T09:30:20.033226"","transactionId": "1","accountId": "1","amount": 100}'
```

## Fraud Detection Rules

#### Large Transaction Rule

#### Rapid Transaction Burst Rule

#### Geographically Improbable Transaction Sequences Rule

## Key Design Decisions around Real-Time Processing


