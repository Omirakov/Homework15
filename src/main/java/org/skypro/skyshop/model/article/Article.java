package org.skypro.skyshop.model.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.skypro.skyshop.model.search.Searchable;

import java.util.Objects;
import java.util.UUID;

public class Article implements Searchable {

    private String title;
    private String text;
    private final UUID id;

    public Article(String title, String text, UUID id) {
        this.title = title;
        this.text = text;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public UUID getId() {
        return id;
    }

    @JsonIgnore
    @Override
    public String getSearchTerm() {
        return title + ": " + text;
    }

    @Override
    public String getContentType() {
        return "ARTICLE";
    }

    @Override
    public String getName() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        return Objects.equals(id, article.id) && Objects.equals(title, article.title) && Objects.equals(text, article.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, text);
    }

    @Override
    public String toString() {
        return "Article{" + "title='" + title + '\'' + ", text='" + text + '\'' + ", id=" + id + '}';
    }
}
