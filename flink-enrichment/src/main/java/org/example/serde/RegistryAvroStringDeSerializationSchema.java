package org.example.serde;

import io.confluent.kafka.schemaregistry.avro.AvroSchemaUtils;
import org.apache.flink.formats.avro.RegistryAvroDeserializationSchema;

import java.util.Map;

public class RegistryAvroStringDeSerializationSchema {

    public static RegistryAvroDeserializationSchema<String> forString(
            String subject,
            String schemaRegistryUrl,
            Map<String, ?> registryConfigs
    ) {

        return new RegistryAvroDeserializationSchema<>(
                String.class,
                AvroSchemaUtils.getSchema("String"),
                new AvroCachedSchemaCoderProvider(
                        subject,
                        schemaRegistryUrl,
                        100,
                        registryConfigs)
        );
    }
}
