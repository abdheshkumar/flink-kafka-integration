package org.example.enrich;

import org.apache.flink.api.common.functions.OpenContext;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;

import java.util.List;

public class AsyncEnrichmentFunction extends RichAsyncFunction<String, String> {

    @Override
    public void open(OpenContext openContext) throws Exception {
        super.open(openContext);
        // Open any connections or resources needed for the async operation
    }

    @Override
    public void asyncInvoke(String s, ResultFuture<String> resultFuture) throws Exception {
        resultFuture.complete(List.of(s.split(",")));
    }
}
