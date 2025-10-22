## Task 3: Real-Time Fraud Detection Engine

### The Task:
Could you build a Spring Boot service that detect potential fraudulent transactions in real-time. This service could consume live transactions via a Kafka subscription, cache recent transaction in memory (or into a Redis store!), analyse transaction patterns, and flag suspicious activities based on predefined or learned behaviours. Suspicious activities could include unusually large transactions, geographically improbable transaction sequences, or rapid transaction bursts - or come up with your own rule that you think indicates fraud.

### Extended Features:
- **Adaptive Learning**: Continuously adjust fraud detection thresholds using historical transaction data, customer feedback, or supervised learning.
- **Audit Logging**: Maintain a detailed audit trail of flagged transactions in PostgreSQL for later analysis and reporting.

### Testing:
- Provide unit tests for transaction evaluation logic.

### Documentation:
- Include a README.md describing how fraud detection rules or models are determined and adjusted, key design decisions around real-time processing, and instructions to run the application.

### Tools and Language:
- Java with Spring Boot is preferred for this task.
- Feel free to leverage open-source libraries for anomaly detection or statistical analysis if needed.

This can be as small or as big as you want to make it - so if you dont have much time to work on this then just make a small attempt and be prepared to talk though the nuances etc.
