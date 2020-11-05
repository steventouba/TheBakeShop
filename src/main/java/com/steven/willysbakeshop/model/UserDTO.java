package com.steven.willysbakeshop.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Set;

@JsonDeserialize(builder = UserDTO.Builder.class)
public class UserDTO {

    private long id;

    @JsonProperty("first-name")
    private final String firstName;

    @JsonProperty("last-name")
    private final String lastName;

    @JsonProperty
    private final String email;

    private final String password;

    private final Set<Product> products;

//    public UserDTO() {}

    private UserDTO(Builder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.password = builder.password;
        this.products = builder.products;
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


//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public void setProducts(Set<Product> products) {
//        this.products = products;
//    }
    @JsonPOJOBuilder
    public static class Builder{

        private long id;
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private Set<Product> products;


        public Builder(String firstName, String lastName, String email) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
        }

        public Builder withId(long id) {
            this.id = id;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder withProducts(Set<Product> products) {
            this.products = products;
            return this;
        }

        public UserDTO build() {
            return new UserDTO(this);
        }
    }

}
