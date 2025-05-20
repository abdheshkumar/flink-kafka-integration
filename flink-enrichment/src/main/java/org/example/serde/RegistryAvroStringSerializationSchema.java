package org.example.serde;

import io.confluent.kafka.schemaregistry.avro.AvroSchemaUtils;
import org.apache.flink.formats.avro.RegistryAvroSerializationSchema;

import java.util.Map;

public class RegistryAvroStringSerializationSchema {

    public static RegistryAvroSerializationSchema<String> forString(
            String subject,
            String schemaRegistryUrl,
            Map<String, ?> registryConfigs
    ) {
        return new RegistryAvroSerializationSchema<>(
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
