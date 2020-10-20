package com.steven.willysbakeshop.repository;

import com.steven.willysbakeshop.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class UserDetails implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);

        if (!user.isPresent()) { throw new UsernameNotFoundException(String.format("Use: %s, does not exist", username)); }


    }
}
