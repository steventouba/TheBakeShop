package com.steven.willysbakeshop.model;

import org.springframework.web.multipart.MultipartFile;

public class ProductRequestDTO {

    private Long id;
    private String name;
    private String description;
    private MultipartFile image;

    public ProductRequestDTO() {

    }

    public ProductRequestDTO(String name, String description, MultipartFile image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public MultipartFile getImage() {
        return image;
    }
}
