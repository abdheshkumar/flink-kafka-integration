package org.example.kafka;

import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction;
import org.apache.flink.util.Collector;

public class JoinLogic extends ProcessJoinFunction<Order, User, OrderByUser> {
    @Override
    public void processElement(Order order, User user, Context ctx, Collector<OrderByUser> out) {
        var orderByUser = new OrderByUser(order.getOrderId(), user.getUserName(), order.getAmount());
        out.collect(orderByUser);
    }
}