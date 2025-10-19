package org.skypro.skyshop.service;

import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.DiscountedProduct;
import org.skypro.skyshop.model.product.FixPriceProduct;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.product.SimpleProduct;
import org.skypro.skyshop.model.search.Searchable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StorageService {

    private final Map<UUID, Product> productMap;
    private final Map<UUID, Article> articleMap;

    public StorageService() {
        this.productMap = new HashMap<>();
        this.articleMap = new HashMap<>();
        initializeTestData();
    }

    public StorageService(Map<UUID, Product> productMap, Map<UUID, Article> articleMap) {
        this.productMap = productMap;
        this.articleMap = articleMap;
    }

    public Map<UUID, Product> getProductMap() {
        return productMap;
    }

    public Map<UUID, Article> getArticleMap() {
        return articleMap;
    }

    private void initializeTestData() {
        productMap.put(UUID.randomUUID(), new SimpleProduct("Apple", UUID.randomUUID(), 100));
        productMap.put(UUID.randomUUID(), new SimpleProduct("Banana", UUID.randomUUID(), 200));
        productMap.put(UUID.randomUUID(), new DiscountedProduct("Orange", UUID.randomUUID(),250, 50));
        productMap.put(UUID.randomUUID(), new SimpleProduct("Oreo", UUID.randomUUID(), 245));
        productMap.put(UUID.randomUUID(), new DiscountedProduct("Grape", UUID.randomUUID(),300, 25));
        productMap.put(UUID.randomUUID(), new FixPriceProduct("Strawberry", UUID.randomUUID()));
        productMap.put(UUID.randomUUID(), new SimpleProduct("MAD", UUID.randomUUID(), 1200));
        productMap.put(UUID.randomUUID(), new FixPriceProduct("New York Times", UUID.randomUUID()));
        productMap.put(UUID.randomUUID(), new DiscountedProduct("Jacobs Monarch", UUID.randomUUID(), 580, 30));

        articleMap.put(UUID.randomUUID(), new Article("Alligators", "Alligators are large, crocodile-like...", UUID.randomUUID()));
        articleMap.put(UUID.randomUUID(), new Article("Morning warm-up", "1. Stretching and circular movements...", UUID.randomUUID()));
    }

    public List<Searchable> getAllSearchable() {
        List<Searchable> result = new ArrayList<>();
        result.addAll(getProductMap().values());
        result.addAll(getArticleMap().values());
        return result;
    }


}
