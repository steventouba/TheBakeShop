package com.steven.willysbakeshop.model;

import java.io.Serializable;

public class ProductDTO implements Serializable {

    private final long seller;
    private final String description;
    private final String name;

    public ProductDTO(String name, String description, long sellerId) {
        this.name = name;
        this.description = description;
        this.seller = sellerId;
    }

    public long getSeller() {
        return seller;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
