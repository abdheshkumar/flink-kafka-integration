package org.example.pizza;

public class PizzaOrder {
    String id;
    String shop;
    Pizza[] pizzas;
    public PizzaOrder(String id, String shop, Pizza[] pizzas) {
        this.id = id;
        this.shop = shop;
        this.pizzas = pizzas;
    }

    public String getId() {
        return id;
    }

    public String getShop() {
        return shop;
    }

    public Pizza[] getPizzas() {
        return pizzas;
    }
    // override toString
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PizzaOrder{");
        sb.append("id='").append(id).append('\'');
        sb.append(", shop='").append(shop).append('\'');
        sb.append(", pizzas=");
        for (Pizza pizza : pizzas) {
            sb.append(pizza.toString()).append(", ");
        }
        sb.append('}');
        return sb.toString();
    }
}
