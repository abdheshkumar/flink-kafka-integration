package org.example.kafka;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class StreamExecutionEnvironmentFactory {
    public static StreamExecutionEnvironment from(boolean isLocal) {
        StreamExecutionEnvironment env;
        if (isLocal) {
            var conf = new Configuration();
            conf.set(RestOptions.PORT, 8083); // local web UI port
            //conf.set(TaskManagerOptions.NUM_TASK_SLOTS, 4);
            //conf.set(TaskManagerOptions.MEMORY_SIZE, "1024m");
            env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        } else {
            env = StreamExecutionEnvironment.getExecutionEnvironment();
        }
        return env;
    }
}
