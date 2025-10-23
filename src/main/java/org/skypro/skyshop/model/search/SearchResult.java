package org.skypro.skyshop.model.search;

import java.util.Objects;
import java.util.UUID;

public class SearchResult {

    private final UUID id;
    private final String name;
    private final String contentType;

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContentType() {
        return contentType;
    }

    public SearchResult(UUID id, String name, String contentType) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null");
        }
        if (name == null) {
            throw new IllegalArgumentException("Name must not be null");
        }
        if (contentType == null) {
            throw new IllegalArgumentException("Content type must not be null");
        }

        this.id = id;
        this.name = name;
        this.contentType = contentType;
    }

    public static SearchResult fromSearchable(Searchable searchable) {
        return new SearchResult(searchable.getId(), searchable.getName(), searchable.getContentType());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchResult that = (SearchResult) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SearchResult{" + "id=" + id + ", name='" + name + '\'' + ", contentType='" + contentType + '\'' + '}';
    }
}
