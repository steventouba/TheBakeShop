package com.steven.willysbakeshop.service;

import com.steven.willysbakeshop.model.MyUserDetails;
import com.steven.willysbakeshop.model.User;
import com.steven.willysbakeshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);

        user.orElseThrow(() -> new UsernameNotFoundException("Not Found:" + username));

        return new MyUserDetails(user.get());
    }
}
