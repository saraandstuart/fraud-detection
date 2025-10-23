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

## How To 
##### Publish a transaction message to Kafka via an HTTP endpoint e.g. using curl
```shell
curl 'http://localhost:8089/v1/transactions' -H 'Content-Type: application/json' --data '{"timeStamp": "2025-10-21T09:30:20.033226","transactionId": "1","accountId": "1","latitude": 54.597286,"longitude": -5.930120,"amount": 100}'
```
##### Read fraud-alert messages via HTTP endpoint
```shell
curl 'http://localhost:8089/v1/fraud-alerts'
```


## Fraud Detection Rules

#### Large Transaction Rule
This checks if the amount of a transaction is greater than 10000.0 regardless of the account id.

#### Rapid Transaction Burst Rule
This checks if there have been more than 5 transactions in the last minute for the same account id.

#### Geographically Improbable Transaction Sequences Rule
This checks if the speed to travel the distance between this transaction and the last one is greater than the speed of an airplane. 

## Key Design Decisions
- I used the strategy pattern for rules so that new rules can be added without changing existing code.
- I used a TransactionContext to separate data fetching from rule logic. This makes rules easier to test and allows for extending the context in the future. 
- I would consider separating out a persistance consumer and a fraud detection consumer, which would be better for scalability.
- I would consider batching up insertions for higher throughput.
- I would consider using straight Spring Data JDBC for better performance.
- A potentially better approach would be to use a stream processing framework like Kafka Streams.


## Potential Improvements
- Make the Large Transaction Rule amount specific to each account
- Implement a dead letter queue for failed transactions
- Add metrics for observability
- Add auth
- Use a versioning tool for the database e.g. flyway or liquidbase
- Use BigDecimal for currency amounts (I used Double to make the code less verbose and easier to follow)
