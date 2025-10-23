package org.skypro.skyshop.model.basket;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ProductBasket {

    private final Map<UUID, Integer> basket = new HashMap<>();

    public ProductBasket() {
    }

    public Map<UUID, Integer> getBasket() {
        return Collections.unmodifiableMap(basket);
    }

    public void addProduct(UUID id) {
        basket.merge(id, 1, Integer::sum);
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
