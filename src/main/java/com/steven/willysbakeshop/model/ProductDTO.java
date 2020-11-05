package com.steven.willysbakeshop.model;

import java.io.Serializable;

public class ProductDTO implements Serializable {

    private long seller;
    private String description;
    private String name;

    public ProductDTO() {

    }

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

    public void setSeller(long seller) {
        this.seller = seller;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }
}
