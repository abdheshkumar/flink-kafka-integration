# Flink Kafka Integration Example

## Requirements
- Java 21
- Maven
- Docker

## Run

1. Start Kafka + Flink:

    ```bash
    docker-compose up -d
    ```

2. Produce test data to `orders` and `users` topics.

3. Run the Flink job:

    ```bash
    mvn compile exec:java -Dexec.mainClass=org.example.kafka.KafkaJoinJob
    ```

## Test

Testcontainers-based integration test is available in `KafkaFlinkJoinIntegrationTest.java`.