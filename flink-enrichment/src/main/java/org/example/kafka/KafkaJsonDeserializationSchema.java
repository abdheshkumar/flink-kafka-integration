package org.example.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.io.IOException;
import java.io.Serial;

public class KafkaJsonDeserializationSchema<T> implements DeserializationSchema<T> {

    @Serial
    private static final long serialVersionUID = 1L;
    private final Class<T> typeClass;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public KafkaJsonDeserializationSchema(Class<T> typeClass) {
        this.typeClass = typeClass;
    }

    @Override
    public T deserialize(byte[] message) throws IOException {
        return objectMapper.readValue(message, typeClass);
    }

    @Override
    public boolean isEndOfStream(T nextElement) {
        return false;
    }

    @Override
    public TypeInformation<T> getProducedType() {
        return TypeInformation.of(typeClass);
    }
}