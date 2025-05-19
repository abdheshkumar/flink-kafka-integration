package org.example.pizza;

import java.util.List;

public class EnrichedPizzaOrder {
    PizzaOrder pizzaOrder;
    List<Pizza> enrichedPizzas;

    public EnrichedPizzaOrder(PizzaOrder pizzaOrder, List<Pizza> enrichedPizzas) {
        this.pizzaOrder = pizzaOrder;
        this.enrichedPizzas = enrichedPizzas;
    }

    // override toString
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EnrichedPizzaOrder{");
        sb.append("pizzaOrder=").append(pizzaOrder.toString());
        sb.append(", enrichedPizzas=");
        for (Pizza pizza : enrichedPizzas) {
            sb.append(pizza.toString()).append(", ");
        }
        sb.append('}');
        return sb.toString();
    }

    static class Pizza {
        String name;
        double price;

        public Pizza(String name, double price) {
            this.name = name;
            this.price = price;
        }


        @Override
        public String toString() {
            return "Pizza{" +
                    "name='" + name + '\'' +
                    ", price='" + price + '\'' +
                    '}';
        }
    }
}
