package com.steven.willysbakeshop.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Set;

@JsonDeserialize(builder = UserDTO.Builder.class)
public class UserDTO {

    private long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final Set<Product> products;

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

    @JsonPOJOBuilder
    public static class Builder{

        private long id;

        @JsonAlias("first-name")
        @JsonProperty
        private String firstName;

        @JsonAlias("last-name")
        @JsonProperty
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
