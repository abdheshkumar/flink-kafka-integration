package org.example.serde;

import org.apache.avro.specific.SpecificRecord;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.formats.avro.RegistryAvroDeserializationSchema;
import org.apache.flink.formats.avro.registry.confluent.ConfluentRegistryAvroDeserializationSchema;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;
import java.util.Map;

public class FlinkStringKeyDeSerializer<V extends SpecificRecord> implements KafkaRecordDeserializationSchema<Tuple2<String, V>> {
    private final Utf8StringDeserializationSchema keySchema;
    private final ConfluentRegistryAvroDeserializationSchema<V> valueSchema;
    private final Class<V> valueType;

    public FlinkStringKeyDeSerializer(Class<V> valueType,
                                      String topic,
                                      String schemaRegistryUrl,
                                      Map<String, ?> registryConfigs) {

        this.valueType = valueType;
        RegistryAvroDeserializationSchema<String> stringRegistryAvroDeserializationSchema = RegistryAvroStringDeSerializationSchema.forString(
                topic + "-key", schemaRegistryUrl, registryConfigs
        );
        // This is a workaround to avoid the unchecked cast warning
        @SuppressWarnings("unchecked")
        DeserializationSchema<Object> innerSchema = (DeserializationSchema<Object>) (DeserializationSchema<?>) stringRegistryAvroDeserializationSchema;

        // Create a new Utf8StringDeserializationSchema with the inner schema so that we can deserialize the key as Utf8 to String
        this.keySchema = new Utf8StringDeserializationSchema(innerSchema);
        this.valueSchema = ConfluentRegistryAvroDeserializationSchema.forSpecific(
                valueType, schemaRegistryUrl, registryConfigs
        );
    }


    @Override
    public void deserialize(ConsumerRecord<byte[], byte[]> consumerRecord, Collector<Tuple2<String, V>> collector) throws IOException {
        String key = keySchema.deserialize(consumerRecord.key());
        V value = valueSchema.deserialize(consumerRecord.value());
        Tuple2<String, V> result = new Tuple2<>(key, value);
        collector.collect(result);
    }

    @Override
    public TypeInformation<Tuple2<String, V>> getProducedType() {
        //return Types.TUPLE(Types.STRING, TypeInformation.of(valueType));
        return Types.TUPLE(keySchema.getProducedType(), valueSchema.getProducedType());
    }
}
