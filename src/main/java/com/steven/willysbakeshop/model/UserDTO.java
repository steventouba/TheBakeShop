package com.steven.willysbakeshop.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.steven.willysbakeshop.controller.UserController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.net.URI;
import java.util.Set;

@JsonDeserialize(builder = UserDTO.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final Set<ProductDTO> products;
    private final String self;

    private UserDTO(Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.password = builder.password;
        this.products = builder.products;
        this.self =  builder.self;
    }

    public Long getId() { return id; }

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

    public Set<ProductDTO> getProducts() { return products; }

    public String getSelf() { return self; }

    @JsonPOJOBuilder
    public static class Builder {

        private Long id;

        @JsonAlias("first-name")
        @JsonProperty
        private String firstName;

        @JsonAlias("last-name")
        @JsonProperty
        private String lastName;

        private String email;
        private String password;
        private Set<ProductDTO> products;
        private String self;

        public Builder(String firstName, String lastName, String email) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder withProducts(Set<ProductDTO> products) {
            this.products = products;
            return this;
        }

        public  Builder withSelfLink(long id) {
            URI location = MvcUriComponentsBuilder
                    .fromMethodName(UserController.class, "getUserById", id)
                    .buildAndExpand(id)
                    .toUri();
            self = location.toString();
            return this;
        }

        public UserDTO build() {
            return new UserDTO(this);
        }
    }

}
