package org.example.pizza;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.List;

public class PizzaOrderStream {
    /*
    Pizza order
    {
  "id": 123,
  "shop": "Mario's kitchen",
  "pizzas": [
    {
      "name": "Diavolo"
    },
    {
      "name": "Hawaii"
    }
  ],
  "timestamp": 12345678
}
     */

    /*
    Pizza Price
    {
  "name": "Diavolo",
  "shop": "Mario's kitchen",
  "price": 14.2,
  "timestamp": 12345678
}
     */

    /*
    Enriched Pizza Order
    {
  "id": 123,
  "shop": "Mario's kitchen",
  "pizzas": [
    {
      "name": "Diavolo",
      "price": 14.2
    },
    {
      "name": "Hawaii",
      "price": 12.5
    }
  ],
  "timestamp": 12345678
}
     */
    static MapStateDescriptor<Tuple2<String, String>, PizzaPrice> pizzaPriceStateDescriptor =
            new MapStateDescriptor<>(
                    "PizzaPriceBroadcastState",
                    TypeInformation.of(new TypeHint<>() {
                    }),
                    TypeInformation.of(PizzaPrice.class));

    public static void main(String[] args) throws Exception {
        // set up streaming execution environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        List<PizzaOrder> pizzaOrders = List.of(
                new PizzaOrder("1", "Mario's kitchen", new Pizza[]{new Pizza("Diavolo")}),
                new PizzaOrder("2", "Luigi's kitchen", new Pizza[]{new Pizza("Hawaii")})
        );

        List<PizzaPrice> pizzaPriceSource = List.of(
                new PizzaPrice("Diavolo", "Mario's kitchen", 14.2, 12345678L),
                new PizzaPrice("Hawaii", "Luigi's kitchen", 12.5, 12345678L)
        );

        DataStream<PizzaOrder> pizzaOrderStream = env.fromData(pizzaOrders)
                .assignTimestampsAndWatermarks(WatermarkStrategy.noWatermarks())
                .map(order -> {
                    Thread.sleep(500); // delay orders slightly to let broadcast populate
                    return order;
                })
                .name("Pizza Order Stream");

        DataStream<PizzaPrice> pizzaPriceStream = env.fromData(pizzaPriceSource).name("PizzaPriceStream");

        BroadcastStream<PizzaPrice> pizzaPriceBroadcastStream = pizzaPriceStream.broadcast(pizzaPriceStateDescriptor);

        DataStream<EnrichedPizzaOrder> enrichedPizzaOrderStream = pizzaOrderStream
                .connect(pizzaPriceBroadcastStream)
                .process(new BroadcastPizzaOrderEnrichmentFunction())
                .name("EnrichedPizzaOrder");

        enrichedPizzaOrderStream.print().name("Print Sink");

        env.execute("Pizza Order Enriching Example");
    }

}
