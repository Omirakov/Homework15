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
        UUID id1 = UUID.randomUUID();
        productMap.put(id1, new SimpleProduct("Apple", id1, 100));

        UUID id2 = UUID.randomUUID();
        productMap.put(id2, new SimpleProduct("Banana", id2, 200));

        UUID id3 = UUID.randomUUID();
        productMap.put(id3, new DiscountedProduct("Orange", id3, 250, 50));

        UUID id4 = UUID.randomUUID();
        productMap.put(id4, new SimpleProduct("Oreo", id4, 245));

        UUID id5 = UUID.randomUUID();
        productMap.put(id5, new DiscountedProduct("Grape", id5, 300, 25));

        UUID id6 = UUID.randomUUID();
        productMap.put(id6, new FixPriceProduct("Strawberry", id6));

        UUID id7 = UUID.randomUUID();
        productMap.put(id7, new SimpleProduct("MAD", id7, 1200));

        UUID id8 = UUID.randomUUID();
        productMap.put(id8, new FixPriceProduct("New York Times", id8));

        UUID id9 = UUID.randomUUID();
        productMap.put(id9, new DiscountedProduct("Jacobs Monarch", id9, 580, 30));

        UUID articleId1 = UUID.randomUUID();
        articleMap.put(articleId1, new Article("Alligators", "Alligators are large, crocodile-like...", articleId1));

        UUID articleId2 = UUID.randomUUID();
        articleMap.put(articleId2, new Article("Morning warm-up", "1. Stretching and circular movements...", articleId2));
    }

    public List<Searchable> getAllSearchable() {
        List<Searchable> result = new ArrayList<>();
        result.addAll(getProductMap().values());
        result.addAll(getArticleMap().values());
        return result;
    }


}
