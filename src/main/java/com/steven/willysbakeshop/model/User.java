package com.steven.willysbakeshop.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
@Table(name = "users", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonProperty("first-name")
    @NotBlank(message = "First Name may not be blank")
    private String firstName;

    @JsonProperty("last-name")
    @NotBlank(message = "Last Name may not be blank")
    private String lastName;

    @JsonProperty
    @NotBlank(message = "Email may not be blank")
//    @Email(message = "Email should be valid")
    private String email;

    @JsonCreator
    public User() {}

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId() &&
                getFirstName().equals(user.getFirstName()) &&
                getLastName().equals(user.getLastName()) &&
                getEmail().equals(user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getLastName(), getEmail());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
