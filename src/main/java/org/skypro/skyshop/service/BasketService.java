package org.skypro.skyshop.service;

import org.skypro.skyshop.exception.NoSuchProductException;
import org.skypro.skyshop.model.basket.BasketItem;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.product.Product;
import org.springframework.stereotype.Service;

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
        storageService.getProductById(id);
        productBasket.addProduct(id);
    }

    public UserBasket getUserBasket() {
        return new UserBasket(
                productBasket.getBasket().entrySet().stream()
                        .map(entry -> {
                            UUID productId = entry.getKey();
                            int count = entry.getValue();
                            Product product = storageService.getProductById(productId);
                            return new BasketItem(product, count);
                        })
                        .collect(Collectors.toList())
        );
    }
}
