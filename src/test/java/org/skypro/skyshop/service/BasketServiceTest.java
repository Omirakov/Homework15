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
import org.skypro.skyshop.model.product.DiscountedProduct;
import org.skypro.skyshop.model.product.FixPriceProduct;
import org.skypro.skyshop.model.product.SimpleProduct;

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

    private final UUID simpleProductId = UUID.randomUUID();
    private final UUID discountedProductId = UUID.randomUUID();
    private final UUID fixPriceProductId = UUID.randomUUID();

    private SimpleProduct simpleProduct;
    private DiscountedProduct discountedProduct;
    private FixPriceProduct fixPriceProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        simpleProduct = new SimpleProduct("Simple Product", simpleProductId, 100);
        discountedProduct = new DiscountedProduct("Discounted Product", discountedProductId, 200, 25);
        fixPriceProduct = new FixPriceProduct("Fix Price Product", fixPriceProductId);
    }

    @Test
    void addProductToBasket_shouldCallStorageAndBasketExactlyOnce_whenProductExists() {
        when(storageService.getProductById(simpleProductId)).thenReturn(simpleProduct);
        basketService.addProductToBasket(simpleProductId);
        verify(storageService, times(1)).getProductById(simpleProductId);
        verify(productBasket, times(1)).addProduct(simpleProductId);
    }

    @Test
    void addProductToBasket_shouldNotModifyBasketAndThrowException_whenProductNotFound() {
        when(storageService.getProductById(simpleProductId)).thenThrow(NoSuchProductException.class);
        assertThrows(NoSuchProductException.class, () -> basketService.addProductToBasket(simpleProductId));
        verify(storageService, times(1)).getProductById(simpleProductId);
        verify(productBasket, never()).addProduct(any());
    }

    @Test
    void addProductToBasket_shouldIncrementBasketCountOnMultipleCalls() {
        when(storageService.getProductById(simpleProductId)).thenReturn(simpleProduct);
        basketService.addProductToBasket(simpleProductId);
        basketService.addProductToBasket(simpleProductId);
        verify(productBasket, times(2)).addProduct(simpleProductId);
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
        when(productBasket.getBasket()).thenReturn(Map.of(simpleProductId, 3));
        when(storageService.getProductById(simpleProductId)).thenReturn(simpleProduct);
        UserBasket result = basketService.getUserBasket();
        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals(300, result.getTotal());
        BasketItem item = result.getItems().get(0);
        assertEquals(simpleProduct, item.getProduct());
        assertEquals(3, item.getCount());
        verify(storageService).getProductById(simpleProductId);
    }

    @Test
    void getUserBasket_shouldReturnMultipleItemsWithCorrectTotal() {
        when(productBasket.getBasket()).thenReturn(Map.of(simpleProductId, 2, discountedProductId, 1, fixPriceProductId, 1));
        when(storageService.getProductById(simpleProductId)).thenReturn(simpleProduct);
        when(storageService.getProductById(discountedProductId)).thenReturn(discountedProduct);
        when(storageService.getProductById(fixPriceProductId)).thenReturn(fixPriceProduct);

        UserBasket result = basketService.getUserBasket();

        assertNotNull(result);
        assertEquals(3, result.getItems().size());
        assertEquals(450, result.getTotal()); // 2*100 + 150 + 100

        assertTrue(result.getItems().stream().anyMatch(i -> i.getProduct().getId().equals(simpleProductId) && i.getCount() == 2));
        assertTrue(result.getItems().stream().anyMatch(i -> i.getProduct().getId().equals(discountedProductId) && i.getCount() == 1));
        assertTrue(result.getItems().stream().anyMatch(i -> i.getProduct().getId().equals(fixPriceProductId) && i.getCount() == 1));
    }

    @Test
    void getUserBasket_shouldThrowExceptionIfAnyProductIsNotFound() {
        UUID missingId = UUID.randomUUID();
        when(productBasket.getBasket()).thenReturn(Map.of(simpleProductId, 1, missingId, 1));
        when(storageService.getProductById(simpleProductId)).thenReturn(simpleProduct);
        when(storageService.getProductById(missingId)).thenThrow(NoSuchProductException.class);

        assertThrows(NoSuchProductException.class, () -> basketService.getUserBasket());

        verify(storageService, times(2)).getProductById(any());
        verify(productBasket).getBasket();
    }

    @Test
    void getUserBasket_shouldPreserveInsertionOrder() {
        Map<UUID, Integer> basketMap = new LinkedHashMap<>();
        basketMap.put(discountedProductId, 1);
        basketMap.put(simpleProductId, 1);
        basketMap.put(fixPriceProductId, 1);

        when(productBasket.getBasket()).thenReturn(basketMap);
        when(storageService.getProductById(simpleProductId)).thenReturn(simpleProduct);
        when(storageService.getProductById(discountedProductId)).thenReturn(discountedProduct);
        when(storageService.getProductById(fixPriceProductId)).thenReturn(fixPriceProduct);

        UserBasket result = basketService.getUserBasket();
        List<BasketItem> items = result.getItems();

        assertEquals(discountedProductId, items.get(0).getProduct().getId());
        assertEquals(simpleProductId, items.get(1).getProduct().getId());
        assertEquals(fixPriceProductId, items.get(2).getProduct().getId());
    }
}