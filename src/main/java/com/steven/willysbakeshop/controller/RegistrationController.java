package com.steven.willysbakeshop.controller;

import com.steven.willysbakeshop.model.*;
import com.steven.willysbakeshop.service.JwtService;
import com.steven.willysbakeshop.service.MyUserDetailsService;
import com.steven.willysbakeshop.service.RegistrationService;
import com.steven.willysbakeshop.util.events.OnRegistrationCompleteEvent;
import com.steven.willysbakeshop.util.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Calendar;
import java.util.Locale;

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
    public ResponseEntity<Void> confirmRegistration(WebRequest webRequest,
                                      @RequestParam("token") String token)
    {
        Locale locale = webRequest.getLocale();

        VerificationToken verificationToken = registrationService.getVerificationToken(token);

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExipryDate().getTime() - cal.getTime().getTime()) <= 0) {
            throw new NotFoundException("Token expired");
        }

        user.setEnabled(true);
        registrationService.saveRegisteredUser(user);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createUser(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        User user = registrationService.registerNewUserAccount(userDTO);

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
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) throws Exception {
        try {
            UsernamePasswordAuthenticationToken authToken
                    = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

            authenticationManager.authenticate(authToken);

        } catch (BadCredentialsException e) {
            throw new Exception("Bad username or password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

}
