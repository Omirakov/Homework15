package org.skypro.skyshop.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.skypro.skyshop.model.search.Searchable;

import java.util.Objects;
import java.util.UUID;

public abstract class Product implements Searchable {

    private final String name;
    private final UUID id;

    public Product(String name, UUID id) {
        this.name = name;
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя не может быть null или пустой строкой");
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getBaseName() {
        return name;
    }

    public abstract int getPrice();

    public abstract boolean isSpecial();

    @JsonIgnore
    @Override
    public String getSearchTerm() {
        return getName();
    }

    @Override
    public String getContentType() {
        return "PRODUCT";
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Product product)) return false;
        return Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

}