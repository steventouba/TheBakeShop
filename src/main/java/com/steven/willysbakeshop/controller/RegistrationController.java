package com.steven.willysbakeshop.controller;

import com.steven.willysbakeshop.model.*;
import com.steven.willysbakeshop.service.JwtService;
import com.steven.willysbakeshop.service.MyUserDetailsService;
import com.steven.willysbakeshop.service.RegistrationService;
import com.steven.willysbakeshop.util.events.OnRegistrationCompleteEvent;
import com.steven.willysbakeshop.util.exceptions.NotFoundException;
import com.steven.willysbakeshop.util.exceptions.TokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Calendar;

@RestController
@RequestMapping("/users")
public class RegistrationController {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/registrationConfirmation")
    public void confirmRegistration(WebRequest webRequest,
                                    @RequestParam("token") String token
    ) throws TokenException {
//        Locale locale = webRequest.getLocale();
        VerificationToken verificationToken = registrationService.getVerificationToken(token);

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExipryDate().getTime() - cal.getTime().getTime()) <= 0) {
            throw new TokenException("Token expired");
        }

        user.setEnabled(true);
        registrationService.saveRegisteredUser(user);
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createUser(@RequestBody UserRequestDTO userRequestDTO,
                                             HttpServletRequest request
    ) {
        User user = registrationService.registerNewUserAccount(userRequestDTO);

        try {
            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request.getLocale(), appUrl));
        } catch (RuntimeException ex) {
            throw new NotFoundException(ex.getMessage());
        }

        URI location = MvcUriComponentsBuilder
                .fromMethodName(UserController.class, "getUserById", user.getId())
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthenticationRequest request)
    throws BadCredentialsException {
        try {
            UsernamePasswordAuthenticationToken authToken
                    = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

            authenticationManager.authenticate(authToken);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }

        MyUserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(userDetails);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        return ResponseEntity.ok()
                .headers(headers)
                .body(userDetails.getFirstName());
    }

}
