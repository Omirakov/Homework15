package org.skypro.skyshop.model.basket;

import java.util.List;

public class UserBasket {

    private final List<BasketItem> items;
    private final int total;

    public UserBasket(List<BasketItem> items) {
        this.items = List.copyOf(items);
        this.total = calculateTotalPrice();
    }

    private int calculateTotalPrice() {
        return (int) items.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getCount()).sum();
    }

    public List<BasketItem> getItems() {
        return items;
    }

    public int getTotal() {
        return total;
    }
}