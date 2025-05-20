package org.example.serde;

import org.apache.avro.specific.SpecificRecord;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.formats.avro.RegistryAvroSerializationSchema;
import org.apache.flink.formats.avro.registry.confluent.ConfluentRegistryAvroSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Map;

public class FlinkStringKeySerializer<V extends SpecificRecord> implements KafkaRecordSerializationSchema<Tuple2<String, V>> {
    private final String topicName;
    private final RegistryAvroSerializationSchema<String> keySchema;
    private final ConfluentRegistryAvroSerializationSchema<V> valueSchema;


    public FlinkStringKeySerializer(Class<V> valueType,
                                    String topic,
                                    String schemaRegistryUrl,
                                    Map<String, ?> registryConfigs) {

        this.topicName = topic;
        this.keySchema = RegistryAvroStringSerializationSchema.forString(
                topic + "-key", schemaRegistryUrl, registryConfigs
        );
        this.valueSchema = ConfluentRegistryAvroSerializationSchema.forSpecific(
                valueType, topic + "-" + valueType.getCanonicalName(), schemaRegistryUrl, registryConfigs
        );
    }

    @Override
    public ProducerRecord<byte[], byte[]> serialize(Tuple2<String, V> keyAndValue, KafkaSinkContext kafkaSinkContext, Long timestamp) {

        try {
            ProducerRecord<byte[], byte[]> record =  new ProducerRecord<>(
                    topicName,
                    null,
                    timestamp == null || timestamp < 0L ? null : timestamp,
                    keySchema.serialize(keyAndValue.f0),
                    valueSchema.serialize(keyAndValue.f1)
            );

            // Add headers to the ProducerRecord
            /*if (keyAndValue.f1 instanceof MyEvent) {
                MyEvent myEvent = (MyEvent) keyAndValue.f1;
                String eventType = myEvent.getEvent().getEventType();
                String eventCategory = myEvent.getEvent().getEventCategory();

                record.headers().add(new RecordHeader("event_type", eventType != null ? eventType.getBytes() : "Unknown".getBytes()));
                record.headers().add(new RecordHeader("event_category", eventCategory != null ? eventCategory.getBytes() : "Unknown".getBytes()));
            }*/

            return   record ;
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not serialize record: " + keyAndValue, e);
        }
    }
}
