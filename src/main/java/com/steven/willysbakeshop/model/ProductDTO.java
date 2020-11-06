package com.steven.willysbakeshop.model;

public class ProductDTO {

    private UserDTO seller;
    private String description;
    private String name;

    public ProductDTO() {

    }

    public ProductDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public ProductDTO(String name, String description, UserDTO seller) {
        this.name = name;
        this.description = description;
        this.seller = seller;
    }


    public UserDTO getSeller() {
        return seller;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setSeller(UserDTO seller) {
        this.seller = seller;
    }

    public void setDescription(String description) { this.description = description; }

    public void setName(String name) {
        this.name = name;
    }
}
