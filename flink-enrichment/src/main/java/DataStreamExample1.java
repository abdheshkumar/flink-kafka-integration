import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.List;

public class DataStreamExample1 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<Integer> source = env.fromData(List.of(1, 2, 3, 4, 5));

        source.name("source")
                .map(v -> v * 2).name("map1")
                .map(v -> v * 3).name("map2")
                .rebalance()
                .map(v -> v * 4).name("map3")
                .map(v -> v * 5).name("map4")
                .keyBy((value) -> value)
                .map(v -> v * 6).name("map5")
                .map(v -> v * 7).name("map6")
                .print().name("sink");

        env.execute("DataStream Example 1");
    }
}
