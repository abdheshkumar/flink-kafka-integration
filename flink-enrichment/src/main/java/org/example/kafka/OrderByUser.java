package org.example.kafka;

import org.apache.flink.api.common.typeinfo.TypeInfo;

@TypeInfo(OrderByUserFactory.class)
public class OrderByUser {
    public OrderByUser() {
        // Default constructor
    }
    public OrderByUser(String orderId, String userName, double amount) {
        this.orderId = orderId;
        this.userName = userName;
        this.amount = amount;
    }

    String orderId;
    String userName;
    double amount;

    @Override
    public String toString() {
        return "OrderByUser{" +
                "orderId='" + orderId + '\'' +
                ", userName='" + userName + '\'' +
                ", amount=" + amount +
                '}';
    }
}