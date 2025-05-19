package org.example.enrich;

import org.apache.flink.api.common.functions.OpenContext;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.util.Collector;
import org.example.kafka.User;

import java.util.Map;
//https://github.com/knaufk/enrichments-with-flink/tree/master
//https://www.waitingforcode.com/apache-flink/data-enrichment-strategies-apache-flink/read
//https://community.aws/content/2oWhYhyuQigQdwiKRDiK6whpqR7/mastering-the-art-of-data-enrichment-with-apache-flink-s02-ep41-lets-talk-about-data
//https://github.com/aws-samples/apache-flink-near-online-data-enrichment-patterns/blob/main/src/test/java/com/amazonaws/samples/stream/temperature/event/TemperatureTest.java
//https://github.com/srakshit/cdc-order-enrichment-flink-example/tree/main
// Use @TypeInfo annotation to specify the type information for the class. This annotation is used by Flink to determine how to serialize and deserialize the class using TypeInfoFactory.

public class EnrichmentWithPreloading extends RichFlatMapFunction<String, User> {

    private Map<String, User> referenceData;

    @Override
    public void open(final OpenContext openContext) throws Exception {
        super.open(openContext);
        referenceData = Map.of();
    }

    @Override
    public void flatMap(
            final String id,
            final Collector<User> collector) throws Exception {
        User sensorReferenceData = referenceData.get(id);
        collector.collect(new User(id, "asa"));
    }
}