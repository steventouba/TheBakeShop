package com.steven.willysbakeshop.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.steven.willysbakeshop.model.User;
import com.steven.willysbakeshop.repository.UserRepository;
import com.steven.willysbakeshop.utilities.exceptions.ErrorDetails;
import com.steven.willysbakeshop.utilities.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/test")
5public class Test {
    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Foo> ping() {
        return ResponseEntity.ok(new Foo("PONG"));
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> getUsers() {
        List<User> all = userRepository.findAll();

        return ResponseEntity.ok(all);

    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<User> getUsers(@PathVariable long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) { throw new UserNotFoundException(String.format("User does not exit: %d", id)); }

        return ResponseEntity.ok(user.get());
    }

    @JacksonXmlRootElement(localName = "Foo")
    public static class Foo {

        @JsonProperty
        private String message;

        public Foo(String message) {
            this.message = message;
        }
    }
}
