package com.steven.willysbakeshop.security;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {

    Authentication getAuthentication();

    boolean isPrincipalMatch(String username);
}
