package org.skypro.skyshop.model.product;

import java.util.UUID;

public class DiscountedProduct extends Product {

    private int basePrice;
    private int discount;

    public DiscountedProduct(String name, UUID id, int basePrice, int discount) {
        super(name, id);
        this.basePrice = basePrice;
        this.discount = discount;
        if (basePrice <= 0) {
            throw new IllegalArgumentException("Базовая цена не может быть меньше 1");
        }
        if (discount < 0 || discount > 100) {
            throw new IllegalArgumentException("Размер скидки должен находиться в диапазоне от 0 до 100 (включительно)");
        }
    }

    @Override
    public int getPrice() {
        return basePrice - (basePrice * discount / 100);
    }

    @Override
    public String toString() {
        return getName() + ": " + getPrice() + "(СКИДКА: " + discount + "%)";
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public String getSearchTerm() {
        return toString();
    }

    @Override
    public String getBaseName() {
        return getName();
    }

}
