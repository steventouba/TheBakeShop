package com.steven.willysbakeshop.model;

public class ProductRequestDTO {

    private Long id;
    private String name;
    private String description;

    public ProductRequestDTO() {

    }

    public ProductRequestDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
