package org.skypro.skyshop.service;

import org.skypro.skyshop.model.search.SearchResult;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private final StorageService storageService;

    public SearchService(StorageService storageService) {
        this.storageService = storageService;
    }

    public List<SearchResult> search(String query) {
        String lowerCaseQuery = query != null ? query.toLowerCase() : "";
        return Optional.ofNullable(storageService.getAllSearchable())
                .orElse(Collections.emptyList())
                .stream()
                .filter(item -> item != null
                        && item.getName() != null
                        && item.getName().toLowerCase().contains(lowerCaseQuery))
                .map(SearchResult::fromSearchable)
                .collect(Collectors.toList());
    }
}
