package org.skypro.skyshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.skypro.skyshop.model.search.SearchResult;
import org.skypro.skyshop.model.search.Searchable;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchServiceTest {

    @Mock
    private StorageService storageService;

    @InjectMocks
    private SearchService searchService;

    private final UUID testId1 = UUID.randomUUID();
    private final UUID testId2 = UUID.randomUUID();
    private final UUID testId3 = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void search_shouldReturnEmptyList_whenQueryIsNull() {
        List<SearchResult> result = searchService.search(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(storageService, never()).getAllSearchable();
    }

    @Test
    void search_shouldReturnEmptyList_whenQueryIsEmptyAfterTrim() {
        List<SearchResult> result = searchService.search("   ");
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(storageService, never()).getAllSearchable();
    }

    @Test
    void search_shouldReturnEmptyList_whenNoMatchingItems() {
        Searchable item = mockSearchable("Laptop for work", "Laptop", "product", testId1);
        when(storageService.getAllSearchable()).thenReturn(List.of(item));
        List<SearchResult> result = searchService.search("gaming");
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(storageService).getAllSearchable();
    }

    @Test
    void search_shouldReturnMatchingResults_caseInsensitive() {
        Searchable product1 = mockSearchable("Apple iPhone 15", "iPhone 15", "product", testId1);
        Searchable product2 = mockSearchable("Green apple juice", "Apple Juice", "product", testId2);
        Searchable article = mockSearchable("How to bake an APPLE pie", "Apple Pie", "article", testId3);
        Searchable unrelated = mockSearchable("Android phone", "Pixel 7", "product", UUID.randomUUID());

        when(storageService.getAllSearchable()).thenReturn(List.of(product1, product2, article, unrelated));

        List<SearchResult> result = searchService.search("apple");

        assertNotNull(result);
        assertEquals(3, result.size());

        assertTrue(result.contains(new SearchResult(testId1, "iPhone 15", "product")));
        assertTrue(result.contains(new SearchResult(testId2, "Apple Juice", "product")));
        assertTrue(result.contains(new SearchResult(testId3, "Apple Pie", "article")));

        assertTrue(result.stream().allMatch(r -> r.getName().toLowerCase().contains("apple")));
    }

    @Test
    void search_shouldHandleNullSearchTermInItems() {
        Searchable validItem = mockSearchable("Normal item", "Normal Product", "product", testId1);
        Searchable nullSearchTermItem = mockSearchable(null, "Null Term Article", "article", testId2);

        when(storageService.getAllSearchable()).thenReturn(List.of(validItem, nullSearchTermItem));

        List<SearchResult> result = searchService.search("normal");

        assertNotNull(result);
        assertEquals(1, result.size());
        SearchResult found = result.get(0);
        assertEquals(testId1, found.getId());
        assertEquals("Normal Product", found.getName());
        assertEquals("product", found.getContentType());
    }

    @Test
    void search_shouldWorkWithSingleCharacterQuery() {
        UUID id = UUID.randomUUID();
        Searchable item = mockSearchable("Zebra crossing", "Zebra", "product", id);
        when(storageService.getAllSearchable()).thenReturn(List.of(item));

        List<SearchResult> result = searchService.search("z");

        assertNotNull(result);
        assertEquals(1, result.size());
        SearchResult found = result.get(0);
        assertEquals(id, found.getId());
        assertEquals("Zebra", found.getName());
        assertEquals("product", found.getContentType());
    }

    private Searchable mockSearchable(String searchTerm, String name, String contentType, UUID id) {
        Searchable searchable = mock(Searchable.class);
        when(searchable.getSearchTerm()).thenReturn(searchTerm);
        when(searchable.getName()).thenReturn(name);
        when(searchable.getContentType()).thenReturn(contentType);
        when(searchable.getId()).thenReturn(id);
        return searchable;
    }
}