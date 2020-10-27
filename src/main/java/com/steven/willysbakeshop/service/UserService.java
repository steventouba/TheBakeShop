package com.steven.willysbakeshop.service;

import com.steven.willysbakeshop.model.User;
import com.steven.willysbakeshop.model.UserDTO;
import com.steven.willysbakeshop.repository.UserRepository;
import com.steven.willysbakeshop.utilities.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Transactional
    public boolean registerNewUserAccount(UserDTO userDTO) {

        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(user);

        return true;
    }

    @Transactional
    public User alterUserAccount(UserDTO userDTO, long id) throws NotFoundException {
        Optional<User> user = userRepository.findById(id);
        user.orElseThrow(() -> new NotFoundException(String.format("User: %d could not be located", id)));

        Optional<User> userStream = user.map(userToEdit -> {
            userToEdit.setFirstName(userDTO.getFirstName());
            userToEdit.setLastName(userDTO.getLastName());
            userToEdit.setEmail(userDTO.getEmail());
            userToEdit.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            return userRepository.save(userToEdit);
        });

        return userStream.get();
    }
}
