package com.steven.willysbakeshop.controller;

import com.steven.willysbakeshop.model.User;
import com.steven.willysbakeshop.repository.UserRepository;
import com.steven.willysbakeshop.utilities.exceptions.UserNotFoundException;
import com.steven.willysbakeshop.utilities.exceptions.UserValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("OK");
    }

    @GetMapping(value = "/")
    public ResponseEntity<List<User>> getUsers() {
        List<User> all = userRepository.findAll();

        return ResponseEntity.ok(all);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> getUsers(@PathVariable long id) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) { throw new UserNotFoundException(String.format("User: %d does not exist", id)); }

        return ResponseEntity.ok(user.get());
    }

    @PostMapping(value = "/create")
    public ResponseEntity<User> createUser(@RequestBody User newUser) throws UserValidationException {
        try {
            User user = userRepository.save(newUser);
            return ResponseEntity.ok(user);
        } catch (ConstraintViolationException e) {
            StringBuilder sb = new StringBuilder();
            e.getConstraintViolations()
                    .stream()
                    .forEach(violation -> {
                        sb.append(violation.getMessage());
                        sb.append(", ");
                    });
            throw new UserValidationException(sb.toString());
        }
    }

    @PutMapping(value = "/{id}/edit")
    public ResponseEntity<User> editUser(@PathVariable long id, @RequestBody User newUser) {
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) { throw new UserNotFoundException(String.format("User: %d could not be located", id)); }

        Optional<User> test = user.map(user1 -> {
            user1.setFirstName(newUser.getFirstName());
            user1.setLastName(newUser.getLastName());
            user1.setEmail(newUser.getEmail());
            return userRepository.save(user1);
        });

        return ResponseEntity.ok(test.get());
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<User> deleteUser(@PathVariable long id) {
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) { throw new UserNotFoundException(String.format("User: %d could not be located", id)); }

        userRepository.delete(user.get());
        return ResponseEntity.ok(user.get());
    }

//    @JacksonXmlRootElement(localName = "Foo")
//    public static class Foo {
//
//        @JsonProperty
//        private String message;
//
//        public Foo(String message) {
//            this.message = message;
//        }
//    }
}
