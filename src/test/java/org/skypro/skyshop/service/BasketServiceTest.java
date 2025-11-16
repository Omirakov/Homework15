package org.skypro.skyshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.skypro.skyshop.exception.NoSuchProductException;
import org.skypro.skyshop.model.basket.BasketItem;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.product.Product;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BasketServiceTest {

    @Mock
    private StorageService storageService;

    @Mock
    private ProductBasket productBasket;

    @InjectMocks
    private BasketService basketService;

    private final UUID productId = UUID.randomUUID();
    private final UUID anotherProductId = UUID.randomUUID();
    private Product product;
    private Product anotherProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new TestProduct("Test Product", productId, 100);
        anotherProduct = new TestProduct("Another Product", anotherProductId, 250);
    }

    @Test
    void addProductToBasket_shouldCallStorageAndBasketExactlyOnce_whenProductExists() {
        when(storageService.getProductById(productId)).thenReturn(product);
        basketService.addProductToBasket(productId);
        verify(storageService, times(1)).getProductById(productId);
        verify(productBasket, times(1)).addProduct(productId);
    }

    @Test
    void addProductToBasket_shouldNotModifyBasketAndThrowException_whenProductNotFound() {
        when(storageService.getProductById(productId)).thenThrow(NoSuchProductException.class);
        assertThrows(NoSuchProductException.class, () -> basketService.addProductToBasket(productId));
        verify(storageService, times(1)).getProductById(productId);
        verify(productBasket, never()).addProduct(any());
    }

    @Test
    void addProductToBasket_shouldIncrementBasketCountOnMultipleCalls() {
        when(storageService.getProductById(productId)).thenReturn(product);
        basketService.addProductToBasket(productId);
        basketService.addProductToBasket(productId);
        verify(productBasket, times(2)).addProduct(productId);
    }

    @Test
    void getUserBasket_shouldReturnEmptyResultWhenBasketIsEmpty() {
        when(productBasket.getBasket()).thenReturn(Map.of());
        UserBasket result = basketService.getUserBasket();
        assertNotNull(result);
        assertTrue(result.getItems().isEmpty());
        assertEquals(0, result.getTotal());
        verify(productBasket, times(1)).getBasket();
    }

    @Test
    void getUserBasket_shouldReturnSingleItemWithCorrectTotal() {
        when(productBasket.getBasket()).thenReturn(Map.of(productId, 3));
        when(storageService.getProductById(productId)).thenReturn(product);
        UserBasket result = basketService.getUserBasket();
        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals(300, result.getTotal());
        BasketItem item = result.getItems().get(0);
        assertEquals(product, item.getProduct());
        assertEquals(3, item.getCount());
        verify(storageService).getProductById(productId);
    }

    @Test
    void getUserBasket_shouldReturnMultipleItemsWithCorrectTotal() {
        when(productBasket.getBasket()).thenReturn(Map.of(productId, 2, anotherProductId, 1));
        when(storageService.getProductById(productId)).thenReturn(product);
        when(storageService.getProductById(anotherProductId)).thenReturn(anotherProduct);
        UserBasket result = basketService.getUserBasket();
        assertNotNull(result);
        assertEquals(2, result.getItems().size());
        assertEquals(450, result.getTotal());
        assertTrue(result.getItems().stream().anyMatch(i -> i.getProduct().getId().equals(productId) && i.getCount() == 2));
        assertTrue(result.getItems().stream().anyMatch(i -> i.getProduct().getId().equals(anotherProductId) && i.getCount() == 1));
    }

    @Test
    void getUserBasket_shouldThrowExceptionIfAnyProductIsNotFound() {
        UUID missingId = UUID.randomUUID();
        when(productBasket.getBasket()).thenReturn(Map.of(productId, 1, missingId, 1));
        when(storageService.getProductById(productId)).thenReturn(product);
        when(storageService.getProductById(missingId)).thenThrow(NoSuchProductException.class);
        assertThrows(NoSuchProductException.class, () -> basketService.getUserBasket());
        verify(storageService, times(2)).getProductById(any());
        verify(productBasket).getBasket();
    }

    @Test
    void getUserBasket_shouldPreserveInsertionOrder() {
        Map<UUID, Integer> basketMap = new LinkedHashMap<>();
        basketMap.put(anotherProductId, 1);
        basketMap.put(productId, 1);
        when(productBasket.getBasket()).thenReturn(basketMap);
        when(storageService.getProductById(productId)).thenReturn(product);
        when(storageService.getProductById(anotherProductId)).thenReturn(anotherProduct);
        UserBasket result = basketService.getUserBasket();
        List<BasketItem> items = result.getItems();
        assertEquals(anotherProductId, items.get(0).getProduct().getId());
        assertEquals(productId, items.get(1).getProduct().getId());
    }

    private static class TestProduct extends Product {
        private final int price;

        public TestProduct(String name, UUID id, int price) {
            super(name, id);
            this.price = price;
        }

        @Override
        public int getPrice() {
            return price;
        }

        @Override
        public boolean isSpecial() {
            return false;
        }
    }
}