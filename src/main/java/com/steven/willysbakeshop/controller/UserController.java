package com.steven.willysbakeshop.controller;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.steven.willysbakeshop.model.User;
import com.steven.willysbakeshop.repository.UserRepository;
import com.steven.willysbakeshop.utilities.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.steven.willysbakeshop.configuration.MediaTypes.TEXT_CSV_VALUE;

@RestController
@RequestMapping("/users")
@Transactional
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public ResponseEntity<User> getUserById(@PathVariable long id) throws NotFoundException {
        Optional<User> user = userRepository.findById(id);
        user.orElseThrow(() -> new NotFoundException(String.format("User: %d does not exist", id)));

        return ResponseEntity.ok(user.get());
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@RequestBody @Valid User newUser)  {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        User user = userRepository.save(newUser);

        return ResponseEntity.ok(user);
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
    public ResponseEntity<User> editUser(@PathVariable long id, @RequestBody @Valid User newUser) throws NotFoundException {
        Optional<User> user = userRepository.findById(id);
        user.orElseThrow(() -> new NotFoundException(String.format("User: %d could not be located", id)));

        Optional<User> userStream = user.map(user1 -> {
            user1.setFirstName(newUser.getFirstName());
            user1.setLastName(newUser.getLastName());
            user1.setEmail(newUser.getEmail());
            user1.setPassword(passwordEncoder.encode(newUser.getPassword()));
            return userRepository.save(user1);
        });

        return ResponseEntity.ok(userStream.get());
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<User> deleteUser(@PathVariable long id) throws NotFoundException {
        Optional<User> user = userRepository.findById(id);
        user.orElseThrow(() -> new NotFoundException(String.format("User: %d could not be located", id)));
        userRepository.delete(user.get());

        return ResponseEntity.ok(user.get());
    }

}
