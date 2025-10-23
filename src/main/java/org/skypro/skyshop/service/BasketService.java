package org.skypro.skyshop.service;

import org.skypro.skyshop.model.basket.BasketItem;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.product.Product;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BasketService {

    private final StorageService storageService;
    private final ProductBasket productBasket;

    public BasketService(StorageService storageService, ProductBasket productBasket) {
        this.storageService = storageService;
        this.productBasket = productBasket;
    }

    public void addProductToBasket(UUID id) {
        Optional<Product> productOptional = storageService.getProductById(id);

        if (productOptional.isEmpty()) {
            throw new IllegalArgumentException("Продукт с ID " + id + " не найден");
        }

        productBasket.addProduct(id);
    }

    public UserBasket getUserBasket() {
        Map<UUID, Integer> itemsMap = productBasket.getBasket();

        return new UserBasket(itemsMap.entrySet().stream().map(entry -> {
            UUID productId = entry.getKey();
            int count = entry.getValue();
            Product product = storageService.getProductById(productId).orElseThrow(() -> new IllegalStateException("Продукт с ID " + productId + " не найден"));

            return new BasketItem(product, count);
        }).collect(Collectors.toList()));
    }
}
