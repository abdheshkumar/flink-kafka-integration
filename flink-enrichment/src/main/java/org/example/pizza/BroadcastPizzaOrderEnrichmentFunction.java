package org.example.pizza;

import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.List;

public class BroadcastPizzaOrderEnrichmentFunction extends BroadcastProcessFunction<PizzaOrder, PizzaPrice,
        EnrichedPizzaOrder> {
    @Override
    // context -> read only!
    public void processElement(PizzaOrder pizzaOrder, ReadOnlyContext ctx, Collector<EnrichedPizzaOrder> out) throws Exception {
        ReadOnlyBroadcastState<Tuple2<String, String>, PizzaPrice> state = ctx.getBroadcastState(PizzaOrderStream.pizzaPriceStateDescriptor);
        List<EnrichedPizzaOrder.Pizza> enrichedPizzas = new ArrayList<>();
        System.out.println("Processing pizza order: " + pizzaOrder);
        for (Pizza pizza : pizzaOrder.getPizzas()) {
            Tuple2<String, String> stateKey = new Tuple2<>(pizzaOrder.getShop(), pizza.getName());
            double pizzaPrice = -1; // filler
            System.out.println("look up key: " + stateKey);
            if (state.contains(stateKey)) {
                pizzaPrice = state.get(stateKey).getPrice();
            }
            enrichedPizzas.add( new EnrichedPizzaOrder.Pizza(pizza.name, pizzaPrice));
        }
        EnrichedPizzaOrder enrichedPizzaOrder = new EnrichedPizzaOrder(pizzaOrder, enrichedPizzas);
        out.collect(enrichedPizzaOrder);
    }

    @Override
    // context -> read and write! - must have some deterministic behaviour across all parallel instances
    public void processBroadcastElement(PizzaPrice pizzaPrice, Context ctx, Collector<EnrichedPizzaOrder> out) throws Exception {
        BroadcastState<Tuple2<String, String>, PizzaPrice> state = ctx.getBroadcastState(PizzaOrderStream.pizzaPriceStateDescriptor);
        Tuple2<String, String> stateKey = new Tuple2<>(pizzaPrice.getShop(), pizzaPrice.getName());
        System.out.println("inserting by key: " + stateKey);
        state.put(stateKey, pizzaPrice);
    }
}