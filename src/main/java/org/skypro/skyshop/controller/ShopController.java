package org.skypro.skyshop.controller;

import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.search.SearchResult;
import org.skypro.skyshop.service.BasketService;
import org.skypro.skyshop.service.SearchService;
import org.skypro.skyshop.service.StorageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class ShopController {

    private final StorageService storageService;
    private final SearchService searchService;
    private final BasketService basketService;

    public ShopController(StorageService storageService, SearchService searchService, BasketService basketService) {
        this.storageService = storageService;
        this.searchService = searchService;
        this.basketService = basketService;
    }

    @GetMapping("/shop/products")
    public List<Product> getAllProducts() {
        return new ArrayList<>(storageService.getProductMap().values());
    }

    @GetMapping("/shop/articles")
    public List<Article> getAllArticles() {
        return new ArrayList<>(storageService.getArticleMap().values());
    }

    @GetMapping("/shop/search")
    public List<SearchResult> search(@RequestParam String pattern) {
        return Optional.ofNullable(searchService.search(pattern)).orElse(Collections.emptyList());
    }

    @GetMapping("/shop/basket/{id}")
    public String addProduct(@PathVariable("id") UUID id) {
        Product product = storageService.getProductById(id).orElse(null);
        if (product == null) {
            return "Продукт с ID " + id + " не найден";
        }
        basketService.addProductToBasket(id);
        return "Продукт успешно добавлен";
    }

    @GetMapping("/shop/basket")
    public UserBasket getUserBasket() {
        return basketService.getUserBasket();
    }
}

