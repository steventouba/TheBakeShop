package com.steven.willysbakeshop.service;

import com.steven.willysbakeshop.model.Role;
import com.steven.willysbakeshop.model.User;
import com.steven.willysbakeshop.model.UserRequestDTO;
import com.steven.willysbakeshop.model.VerificationToken;
import com.steven.willysbakeshop.repository.TokenRepository;
import com.steven.willysbakeshop.repository.UserRepository;
import com.steven.willysbakeshop.util.exceptions.AlreadyExistsException;
import com.steven.willysbakeshop.util.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class RegistrationService {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Transactional
    public User registerNewUserAccount(UserRequestDTO userRequestDTO) throws AlreadyExistsException {
        if (emailExists(userRequestDTO.getEmail())) {
            throw new AlreadyExistsException("There is an account with that email address");
        }

        User user = new User();
        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        user.setRoles(new Role("ROLE_SELLER"));
        userRepository.save(user);

        return user;

    }

    public User getUser(String VerificationToken) {
        Optional<com.steven.willysbakeshop.model.VerificationToken> token
                = tokenRepository.findByToken(VerificationToken);
        token.orElseThrow(() -> new NotFoundException("Could not locate token"));

        return token.get().getUser();
    }

    public VerificationToken getVerificationToken(String VerificationToken) {
        Optional<VerificationToken> token = tokenRepository.findByToken(VerificationToken);
        token.orElseThrow(() -> new NotFoundException("Could not locate token"));

        return token.get();
    }

    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    public void saveRegisteredUser(User user) {
        userRepository.save(user);
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

}
