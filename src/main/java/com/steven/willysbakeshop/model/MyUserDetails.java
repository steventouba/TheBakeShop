package com.steven.willysbakeshop.model;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

public class MyUserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private  String password;
    private  String lastName;
    private  String firstName;
    private String username;
    private boolean enabled;
    private Set<Role> roles;

    public MyUserDetails(String username) {
        this.username = username;
    }

    public MyUserDetails() {}

    public MyUserDetails(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.enabled = user.isEnabled();
        this.roles = user.getRoles();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        }

        return authorities;
    }

    public String getFirstName() { return firstName; }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
