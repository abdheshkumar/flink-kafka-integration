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

/*
1- Flink's Built-in / POJO Serializer
If your class is a Java-style POJO:

Public no-arg constructor

Non-final, public fields or getters/setters

Flink uses its optimized POJO serializer (much faster than Kryo).

2. Fallback: Kryo
If Flink can't identify your object as a POJO (e.g., case class, anonymous class, complex generics):

It falls back to Kryo serialization.

3. Kryo Requires Registration in Strict Mode
By default, Flink uses Kryo in non-strict mode, but in production, you should register custom types:
env.getConfig().registerKryoType(OrderByUser.class);

POJO serialization is 2–5X faster than Kryo.
Kryo creates more GC pressure.
 */