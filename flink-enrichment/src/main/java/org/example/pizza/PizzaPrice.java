package org.example.pizza;

public class PizzaPrice {
    String name;
    String shop;
    Double price;
    Long timestamp;
    public PizzaPrice(String name, String shop, double price, long timestamp) {
        this.name = name;
        this.shop = shop;
        this.price = price;
        this.timestamp = timestamp;
    }

    public Double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getShop() {
        return shop;
    }

    public Long getTimestamp() {
        return timestamp;
    }
    // override toString
    @Override
    public String toString() {
        return "PizzaPrice{" +
                "name='" + name + '\'' +
                ", shop='" + shop + '\'' +
                ", price=" + price +
                ", timestamp=" + timestamp +
                '}';
    }
}
