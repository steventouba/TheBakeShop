package com.steven.willysbakeshop.controller;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.steven.willysbakeshop.model.User;
import com.steven.willysbakeshop.repository.UserRepository;
import com.steven.willysbakeshop.utilities.exceptions.UserNotFoundException;
import com.steven.willysbakeshop.utilities.exceptions.UserValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Transactional
public class UserController {
    public static final MediaType TEXT_CSV = new MediaType("text", "csv");
    public static final String TEXT_CSV_VALUE = "text/csv";

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
    public ResponseEntity<User> getUserById(@PathVariable long id) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) { throw new UserNotFoundException(String.format("User: %d does not exist", id)); }

        return ResponseEntity.ok(user.get());
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@RequestBody User newUser) throws UserValidationException {

        try {
            User user = userRepository.save(newUser);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            throw new UserValidationException("validation error");
        }
    }

    @PostMapping(value = "/create", consumes= TEXT_CSV_VALUE)
    public ResponseEntity<String> createProducts(@RequestBody String csv) throws IOException {

        MappingIterator<User> iterator = new CsvMapper()
                .readerFor(User.class)
                .with(CsvSchema.emptySchema().withHeader())
                .readValues(csv);

        while (iterator.hasNext()) {
            userRepository.save(iterator.next());
        }

        return ResponseEntity.ok("OK");
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
