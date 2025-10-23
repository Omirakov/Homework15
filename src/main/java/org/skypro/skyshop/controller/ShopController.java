package org.skypro.skyshop.controller;

import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.search.SearchResult;
import org.skypro.skyshop.service.SearchService;
import org.skypro.skyshop.service.StorageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class ShopController {

    private final StorageService storageService;
    private final SearchService searchService;

    public ShopController(StorageService storageService, SearchService searchService) {
        this.storageService = storageService;
        this.searchService = searchService;
    }

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return new ArrayList<>(storageService.getProductMap().values());
    }

    @GetMapping("/articles")
    public List<Article> getAllArticles() {
        return new ArrayList<>(storageService.getArticleMap().values());
    }

    @GetMapping("/search")
    public List<SearchResult> search(@RequestParam String pattern) {
        return Optional.ofNullable(searchService.search(pattern)).orElse(Collections.emptyList());
    }
}

