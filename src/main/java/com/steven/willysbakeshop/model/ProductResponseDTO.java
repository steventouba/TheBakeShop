package com.steven.willysbakeshop.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.steven.willysbakeshop.controller.ProductController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@JsonDeserialize(builder = ProductResponseDTO.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponseDTO {

    private final Long id;
    private final String name;
    private final String description;
    private final UserResponseDTO seller;
    private final Map<String, String> resources;

    private ProductResponseDTO(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.seller = builder.seller;
        this.resources = builder.resources;
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

    public UserResponseDTO getSeller() {
        return seller;
    }

    public Map<String, String> getResources() {
        return resources;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductResponseDTO)) return false;
        ProductResponseDTO that = (ProductResponseDTO) o;
        return getId().equals(that.getId()) &&
                getName().equals(that.getName()) &&
                getDescription().equals(that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription());
    }

    @JsonPOJOBuilder
    public static class Builder {

        private long id;
        private String name;
        private String description;
        private UserResponseDTO seller;
        private Map<String, String> resources = new HashMap<>();

        public Builder(long id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        public Builder withSeller(UserResponseDTO seller) {
            this.seller = seller;
            return this;
        }

        public Builder withImageUrl(String url) {
            resources.put("image", url);
            return this;
        }

        public Builder withSelfUrl(long id) {
            URI location = MvcUriComponentsBuilder
                    .fromMethodName(ProductController.class, "getProductById", id)
                    .buildAndExpand(id)
                    .toUri();
            resources.put("self", location.toString());
            return this;
        }

        public ProductResponseDTO build() {
            return new ProductResponseDTO(this);
        }
    }
}
