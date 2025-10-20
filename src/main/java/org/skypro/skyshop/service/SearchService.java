package org.skypro.skyshop.service;

import org.skypro.skyshop.model.search.SearchResult;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private final StorageService storageService;

    public SearchService(StorageService storageService) {
        this.storageService = storageService;
    }

    public List<SearchResult> search(String query) {
        String trimmedQuery = query == null ? "" : query.trim();
        if (trimmedQuery.isEmpty()) {
            return Collections.emptyList();
        }

        String lowerCaseQuery = trimmedQuery.toLowerCase();

        return storageService.getAllSearchable().stream().filter(item -> item != null && item.getSearchTerm() != null && item.getSearchTerm().toLowerCase().contains(lowerCaseQuery)).map(SearchResult::fromSearchable).collect(Collectors.toList());
    }
}
