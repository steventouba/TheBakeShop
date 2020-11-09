package com.steven.willysbakeshop.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "roles", schema = "public")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ERole role;

    @JsonCreator
    public Role() {

    }

    public Role(ERole role) {
    this.role = role;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ERole getRole() {
        return role;
    }

    public void setRole(ERole role) {
        this.role = role;
    }
}
