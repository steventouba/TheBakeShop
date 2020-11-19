package com.steven.willysbakeshop.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequestDTO {

    private Long id;

    @JsonAlias("first-name")
    @JsonProperty
    private String firstName;

    @JsonAlias("last-name")
    @JsonProperty
    private String lastName;

    @JsonAlias("username")
    @JsonProperty
    private String email;

    private String password;

    public UserRequestDTO() {

    }

    public UserRequestDTO(Long id, String firstName, String lastName, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
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

}
