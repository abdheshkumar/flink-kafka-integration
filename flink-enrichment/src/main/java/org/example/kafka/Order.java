package org.example.kafka;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Order {
    @JsonCreator
    public Order(@JsonProperty("order_id") String orderId,
                 @JsonProperty("user_id") String userId,
                 @JsonProperty("amount") Double amount) {
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
    }

    public String orderId;
    public String userId;

    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public Double getAmount() {
        return amount;
    }

    public Double amount;
}