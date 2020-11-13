package com.steven.willysbakeshop.model;

public class ProductDTO {

    private Long id;
    private String description;
    private String name;
    private UserDTO seller;

    public ProductDTO() {

    }

    public ProductDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public ProductDTO(Long id, String name, String description, UserDTO seller) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.seller = seller;
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


    public UserDTO getSeller() {
        return seller;
    }

    public void setSeller(UserDTO seller) {
        this.seller = seller;
    }

}
