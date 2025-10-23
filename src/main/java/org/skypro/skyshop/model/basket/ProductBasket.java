package org.skypro.skyshop.model.basket;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductBasket {

    private final Map<UUID, Integer> basket = new HashMap<>();

    public ProductBasket() {
    }

    public Map<UUID, Integer> getBasket() {
        return Collections.unmodifiableMap(basket);
    }

    public void addProduct(UUID id) {
        basket.put(id, 1);
    }

    public void printBasket() {
        System.out.println("___________________ Продукты в корзине: ___________________");
        if (basket.isEmpty()) {
            System.out.println("В корзине пусто!");
            return;
        }

        basket.forEach((id, count) -> System.out.println("ID: " + id + ", Количество: " + count));
    }

}
