package com.steven.willysbakeshop.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.steven.willysbakeshop.controller.UserController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDTO {

    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final Set<ProductResponseDTO> products;
    private final Map<String, String> resources;

    private UserResponseDTO(Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.password = builder.password;
        this.products = builder.products;
        this.resources =  builder.resources;
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

    public Set<ProductResponseDTO> getProducts() { return products; }

    public Map<String, String> getResources() { return resources; }


    public static class Builder {

        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private Set<ProductResponseDTO> products;
        private Map<String, String> resources = new HashMap<>();

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

        public Builder withProducts(Set<ProductResponseDTO> products) {
            this.products = products;
            return this;
        }

        public  Builder withSelfLink(long id) {
            URI location = MvcUriComponentsBuilder
                    .fromMethodName(UserController.class, "getUserById", id)
                    .buildAndExpand(id)
                    .toUri();
            resources.put("self", location.toString());
            return this;
        }

        public UserResponseDTO build() {
            return new UserResponseDTO(this);
        }
    }

}
