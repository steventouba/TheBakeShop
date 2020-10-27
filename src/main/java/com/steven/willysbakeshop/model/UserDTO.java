package com.steven.willysbakeshop.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Set;

public class UserDTO implements Serializable {

    private long id;

    @JsonProperty("first-name")
    @NotBlank(message = "First Name may not be blank")
    private String firstName;

    @JsonProperty("last-name")
    @NotBlank(message = "Last Name may not be blank")
    private String lastName;

    @JsonProperty
    @NotBlank(message = "Email may not be blank")
    private String email;

    private String password;

    private Set<Product> products;

    public UserDTO() {}

    public UserDTO(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public UserDTO(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public long getId() { return id; }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void  setId(long id) { this.id = id; }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

}
