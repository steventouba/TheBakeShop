package com.steven.willysbakeshop.service;

import com.steven.willysbakeshop.model.User;
import com.steven.willysbakeshop.model.UserDTO;
import com.steven.willysbakeshop.repository.UserRepository;
import com.steven.willysbakeshop.utilities.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
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
                .map(user -> {
                    UserDTO userDTO = new UserDTO();
                    userDTO.setFirstName(user.getFirstName());
                    userDTO.setLastName(user.getLastName());
                    userDTO.setEmail(user.getEmail());
                    return userDTO;
                })
                .collect(Collectors.toList());
    }

    public UserDTO findById(long id) throws  NotFoundException {
        Optional<User> user = userRepository.findById(id);

        user.orElseThrow(() -> new NotFoundException("Could not locate userDTO"));

        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(user.get().getFirstName());
        userDTO.setLastName(user.get().getLastName());
        userDTO.setEmail(user.get().getEmail());

        return userDTO;
    }

    @Transactional
    public UserDTO registerNewUserAccount(UserDTO userDTO) {

        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(user);

        userDTO.setPassword("");
        return userDTO;
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
}
