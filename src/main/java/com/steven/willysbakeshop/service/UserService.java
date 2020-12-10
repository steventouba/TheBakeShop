package com.steven.willysbakeshop.service;

import com.steven.willysbakeshop.model.*;
import com.steven.willysbakeshop.repository.UserRepository;
import com.steven.willysbakeshop.security.AuthenticationFacade;
import com.steven.willysbakeshop.util.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    AuthenticationFacade authenticationFacade;

    @Autowired
    UserRepository userRepository;

    public List<UserResponseDTO> findAll() {
        List<User> users = userRepository.findAll();

      return users
                .stream()
                .map(user -> new UserResponseDTO.Builder(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail()
                )
                        .withId(user.getId())
                        .withSelfLink(user.getId())
                        .build())
                .collect(Collectors.toList());
    }

    public UserResponseDTO findById(long id) throws  NotFoundException {
        Optional<User> user = userRepository.findById(id);

        user.orElseThrow(() -> new NotFoundException("Could not locate user"));

        return new UserResponseDTO.Builder(
                user.get().getFirstName(),
                user.get().getLastName(),
                user.get().getEmail()
        )
                .withProducts(mapProductsToUser(user.get().getProducts()))
                .build();
    }


    @Transactional
    public UserResponseDTO alterUserAccount(UserRequestDTO requestDTO, long id) throws NotFoundException {
        Optional<User> user = userRepository.findById(id);
        user.orElseThrow(() -> new NotFoundException(String.format("User: %d could not be located", id)));

        Optional<User> userStream = user.map(userToEdit -> {
            userToEdit.setFirstName(requestDTO.getFirstName());
            userToEdit.setLastName(requestDTO.getLastName());
            userToEdit.setEmail(requestDTO.getEmail());
            return userRepository.save(userToEdit);
        });

        return new UserResponseDTO.Builder(
                requestDTO.getFirstName(),
                requestDTO.getLastName(),
                requestDTO.getEmail()
        )
                .withId(userStream.get().getId())
                .withSelfLink(userStream.get().getId())
                .build();
    }

    @Transactional
    public void deleteUser(long id) {
        Optional<User> optional = userRepository.findById(id);
        optional.orElseThrow(() -> new NotFoundException("Could not locate requested user"));

        User user = optional.get();
        if (!authenticationFacade.isPrincipalMatch(user.getEmail())) {
            throw new BadCredentialsException("Unauthorized");
        }
        userRepository.delete(user);
    }

    private Set<ProductResponseDTO> mapProductsToUser(Set<Product> products) {
       return products
                .stream()
                .map(product -> new ProductResponseDTO
                        .Builder(product.getId(), product.getName(), product.getDescription())
                        .withSelfUrl(product.getId())
                        .build()
                ).collect(Collectors.toSet());
    }

}
