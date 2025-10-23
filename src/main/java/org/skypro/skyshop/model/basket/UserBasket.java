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
        return items.stream().mapToInt(item -> item.getProduct().getPrice() * item.getCount()).sum();
    }

}
