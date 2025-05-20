package org.example.serde;

import org.apache.flink.api.common.serialization.AbstractDeserializationSchema;
import org.apache.flink.api.common.serialization.DeserializationSchema;

import java.io.IOException;

public class Utf8StringDeserializationSchema extends AbstractDeserializationSchema<String> {

    private final DeserializationSchema<Object> delegate;

    public Utf8StringDeserializationSchema(DeserializationSchema<Object> delegate) {
        this.delegate = delegate;
    }

    @Override
    public String deserialize(byte[] message) throws IOException {
        Object obj = delegate.deserialize(message);
        if (obj instanceof org.apache.avro.util.Utf8) {
            return obj.toString(); // Convert Utf8 to String
        } else if (obj instanceof String) {
            return (String) obj;
        } else {
            throw new IOException("Unexpected type: " + obj.getClass());
        }
    }
}
