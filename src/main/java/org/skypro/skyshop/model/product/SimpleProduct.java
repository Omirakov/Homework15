package org.skypro.skyshop.model.product;

import java.util.UUID;

public class SimpleProduct extends Product {

    private int price;

    public SimpleProduct(String name, UUID id, int price) {
        super(name, id);
        this.price = price;
        if (price <= 0) {
            throw new IllegalArgumentException("Цена не может быть меньше 1");
        }
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return getName() + ": " + getPrice();
    }

    @Override
    public boolean isSpecial() {
        return false;
    }

}
