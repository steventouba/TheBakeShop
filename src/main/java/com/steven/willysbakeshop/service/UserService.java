package com.steven.willysbakeshop.service;

import com.steven.willysbakeshop.model.*;
import com.steven.willysbakeshop.repository.UserRepository;
import com.steven.willysbakeshop.util.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();

      return users
                .stream()
                .map(user -> new UserDTO.Builder(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail()
                )
                        .withId(user.getId())
                        .withSelfLink(user.getId())
                        .build())
                .collect(Collectors.toList());
    }

    public UserDTO findById(long id) throws  NotFoundException {
        Optional<User> user = userRepository.findById(id);

        user.orElseThrow(() -> new NotFoundException("Could not locate userDTO"));

        return new UserDTO.Builder(
                user.get().getFirstName(),
                user.get().getLastName(),
                user.get().getEmail()
        )
                .withProducts(mapProductsToUser(user.get().getProducts()))
                .build();
    }

    @Transactional
    public UserDTO registerNewUserAccount(UserDTO userDTO) {

        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRoles(new Role("ROLE_BUYER"));
        userRepository.save(user);

        return new UserDTO.Builder(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        )
                .withId(user.getId())
                .build();
    }

    @Transactional
    public UserDTO alterUserAccount(UserDTO userDTO, long id) throws NotFoundException {
        Optional<User> user = userRepository.findById(id);
        user.orElseThrow(() -> new NotFoundException(String.format("User: %d could not be located", id)));

        Optional<User> userStream = user.map(userToEdit -> {
            userToEdit.setFirstName(userDTO.getFirstName());
            userToEdit.setLastName(userDTO.getLastName());
            userToEdit.setEmail(userDTO.getEmail());
            return userRepository.save(userToEdit);
        });

        return userDTO;
    }

    private Set<ProductDTO> mapProductsToUser(Set<Product> products) {
       return products
                .stream()
                .map(product -> new ProductDTO(product.getName(), product.getDescription()))
                .collect(Collectors.toSet());
    }
}
