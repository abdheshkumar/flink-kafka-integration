package org.example.serde;

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import org.apache.flink.formats.avro.SchemaCoder;
import org.apache.flink.formats.avro.registry.confluent.ConfluentSchemaRegistryCoder;

import javax.annotation.Nullable;
import java.io.Serial;
import java.util.Map;
import java.util.Objects;

public class AvroCachedSchemaCoderProvider implements SchemaCoder.SchemaCoderProvider {

    @Serial
    private static final long serialVersionUID = 8610401613495438381L;
    private final String subject;
    private final String url;
    private final int cacheCapacity;
    private final @Nullable Map<String, ?> registryConfigs;

    AvroCachedSchemaCoderProvider(
            @Nullable String subject,
            String url,
            int cacheCapacity,
            @Nullable Map<String, ?> registryConfigs) {
        this.subject = subject;
        this.url = Objects.requireNonNull(url);
        this.cacheCapacity = cacheCapacity;
        this.registryConfigs = registryConfigs;
    }

    @Override
    public SchemaCoder get() {
        return new ConfluentSchemaRegistryCoder(
                this.subject,
                new CachedSchemaRegistryClient(url, cacheCapacity, registryConfigs));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AvroCachedSchemaCoderProvider that = (AvroCachedSchemaCoderProvider) o;
        return cacheCapacity == that.cacheCapacity
                && Objects.equals(subject, that.subject)
                && url.equals(that.url)
                && Objects.equals(registryConfigs, that.registryConfigs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, url, cacheCapacity, registryConfigs);
    }
}
