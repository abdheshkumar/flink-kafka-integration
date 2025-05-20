package org.example.kafka.avro;

import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import com.user.User;
import org.example.kafka.StreamExecutionEnvironmentFactory;
import org.example.serde.FlinkStringKeyDeSerializer;
import org.example.serde.FlinkStringKeySerializer;

import java.util.List;
import java.util.Map;

public class KafkaAvro {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironmentFactory.from(true);
        String schemaRegistryUrl = "http://localhost:8082";
        String topic = "user-avro";
        //Producer stream
        DataStreamSource<Tuple2<String, User>> stream = env.fromData(List.of(
                Tuple2.of("user1", User.newBuilder().setName("user1").setAge(20).build()),
                Tuple2.of("user2", User.newBuilder().setName("user2").setAge(30).build()),
                Tuple2.of("user3", User.newBuilder().setName("user3").setAge(40).build()))
        );

        KafkaSink<Tuple2<String, User>> producerStream = KafkaSink.<Tuple2<String, User>>builder()
                .setBootstrapServers("localhost:9092")
                .setRecordSerializer(new FlinkStringKeySerializer<>(User.class, topic, schemaRegistryUrl, Map.of(KafkaAvroSerializerConfig.AUTO_REGISTER_SCHEMAS, true)))
                .setDeliveryGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
                .build();

        DataStreamSink<Tuple2<String, User>> streamSink = stream.sinkTo(producerStream).name("Producing Kafka Avro");

        // Consumer stream
        KafkaSource<Tuple2<String, User>> userConsumer = KafkaSource.<Tuple2<String, User>>builder()
                .setBootstrapServers("localhost:9092")
                .setTopics(topic)
                .setGroupId("flink-kafka-avro-group")
                //.setValueOnlyDeserializer(ConfluentRegistryAvroDeserializationSchema.forSpecific(User.class, schemaRegistryUrl);)
                .setDeserializer(new FlinkStringKeyDeSerializer<>(User.class, topic, schemaRegistryUrl, Map.of(KafkaAvroSerializerConfig.AUTO_REGISTER_SCHEMAS, true)))
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setClientIdPrefix("flink-users-avro-client")
                .build();

        DataStream<Tuple2<String, User>> received = env
                .fromSource(userConsumer, WatermarkStrategy.noWatermarks(), "users-avro-source");

        DataStreamSink<Tuple2<String, User>> sink = received.print();
        env.execute("Flink Avro Round-Trip Example");

    }
}
