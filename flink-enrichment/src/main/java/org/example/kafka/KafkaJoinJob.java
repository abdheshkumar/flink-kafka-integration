package org.example.kafka;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeutils.TypeSerializer;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.configuration.TaskManagerOptions;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.datastream.DataStream;

import java.time.Duration;

public class KafkaJoinJob {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env;
        boolean isLocal = true; // Set to false for production
        if (isLocal) {
            var conf = new Configuration();
            conf.set(RestOptions.PORT, 8082); // local web UI port
            //conf.set(TaskManagerOptions.NUM_TASK_SLOTS, 4);
            //conf.set(TaskManagerOptions.MEMORY_SIZE, "1024m");
            env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        } else {
            env = StreamExecutionEnvironment.getExecutionEnvironment();
        }

        /*
        KafkaSource.builder()
    // Start from committed offset of the consuming group, without reset strategy
    .setStartingOffsets(OffsetsInitializer.committedOffsets())
    // Start from committed offset, also use EARLIEST as reset strategy if committed offset doesn't exist
    .setStartingOffsets(OffsetsInitializer.committedOffsets(OffsetResetStrategy.EARLIEST))
    // Start from the first record whose timestamp is greater than or equals a timestamp (milliseconds)
    .setStartingOffsets(OffsetsInitializer.timestamp(1657256176000L))
    // Start from earliest offset
    .setStartingOffsets(OffsetsInitializer.earliest())
    // Start from latest offset
    .setStartingOffsets(OffsetsInitializer.latest());
         */
        KafkaSource<Order> orderConsumer = KafkaSource.<Order>builder()
                .setBootstrapServers("localhost:9092")
                .setTopics("orders")
                .setGroupId("flink-kafka-group")
                .setValueOnlyDeserializer(new KafkaDeserializationSchema<>(Order.class))
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setClientIdPrefix("flink-orders-client")
                .build();

        KafkaSource<User> userConsumer = KafkaSource.<User>builder()
                .setBootstrapServers("localhost:9092")
                .setTopics("users")
                .setGroupId("flink-kafka-group")
                .setValueOnlyDeserializer(new KafkaDeserializationSchema<>(User.class))
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setClientIdPrefix("flink-users-client")
                .build();

        DataStream<Order> orders = env
                .fromSource(orderConsumer, WatermarkStrategy.noWatermarks(), "orders-source");

        DataStream<User> users = env
                .fromSource(userConsumer, WatermarkStrategy.noWatermarks(), "users-source");

        /*
        // Check which serializer is used by process
        TypeInformation<OrderByUser> typeInfo = TypeInformation.of(OrderByUser.class);
        TypeSerializer<OrderByUser> serializer = typeInfo.createSerializer(env.getConfig().getSerializerConfig());
        System.out.println(serializer);
         */
        orders
                .keyBy(Order::getUserId)
                .intervalJoin(users.keyBy(User::getUserId))
                .between(Duration.ofSeconds(-10), Duration.ofSeconds(10))
                .process(new JoinLogic())
                .print();

        env.execute("Flink Kafka Join Job");
    }
}