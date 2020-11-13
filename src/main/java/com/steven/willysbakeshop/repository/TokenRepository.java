package com.steven.willysbakeshop.repository;

import com.steven.willysbakeshop.model.User;
import com.steven.willysbakeshop.model.VerificationToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);
    Optional<VerificationToken> findByUser(User user);
}
