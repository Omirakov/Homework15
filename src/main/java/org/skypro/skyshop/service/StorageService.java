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
    private final Map<UUID, Product> availableProducts;

    public StorageService() {
        this.productMap = new HashMap<>();
        this.articleMap = new HashMap<>();
        this.availableProducts = new HashMap<>();
        initializeTestData();
    }

    public StorageService(Map<UUID, Product> productMap, Map<UUID, Article> articleMap, Map<UUID, Product> availableProducts) {
        this.productMap = productMap;
        this.articleMap = articleMap;
        this.availableProducts = availableProducts;
    }

    public Map<UUID, Product> getProductMap() {
        return productMap;
    }

    public Map<UUID, Article> getArticleMap() {
        return articleMap;
    }

    private void initializeTestData() {
        UUID id1 = UUID.randomUUID();
        Product apple = new SimpleProduct("Apple", id1, 100);
        productMap.put(id1, apple);
        availableProducts.put(id1, apple);

        UUID id2 = UUID.randomUUID();
        Product banana = new SimpleProduct("Banana", id2, 200);
        productMap.put(id2, banana);
        availableProducts.put(id2, banana);

        UUID id3 = UUID.randomUUID();
        Product orange = new DiscountedProduct("Orange", id3, 250, 50);
        productMap.put(id3, orange);
        availableProducts.put(id3, orange);

        UUID id4 = UUID.randomUUID();
        Product oreo = new SimpleProduct("Oreo", id4, 245);
        productMap.put(id4, oreo);
        availableProducts.put(id4, oreo);

        UUID id5 = UUID.randomUUID();
        Product grape = new DiscountedProduct("Grape", id5, 300, 25);
        productMap.put(id5, grape);
        availableProducts.put(id5, grape);

        UUID id6 = UUID.randomUUID();
        Product strawberry = new FixPriceProduct("Strawberry", id6);
        productMap.put(id6, strawberry);
        availableProducts.put(id6, strawberry);

        UUID id7 = UUID.randomUUID();
        Product mad = new SimpleProduct("MAD", id7, 1200);
        productMap.put(id7, mad);
        availableProducts.put(id7, mad);

        UUID id8 = UUID.randomUUID();
        Product newYorkTimes = new FixPriceProduct("New York Times", id8);
        productMap.put(id8, newYorkTimes);
        availableProducts.put(id8, newYorkTimes);

        UUID id9 = UUID.randomUUID();
        Product jacobsMonarch = new DiscountedProduct("Jacobs Monarch", id9, 580, 30);
        productMap.put(id9, jacobsMonarch);
        availableProducts.put(id9, jacobsMonarch);

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

    public Optional<Product> getProductById(UUID id) {
        return Optional.ofNullable(availableProducts.get(id));
    }

}
