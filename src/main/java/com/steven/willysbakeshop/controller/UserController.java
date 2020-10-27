package com.steven.willysbakeshop.controller;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.steven.willysbakeshop.model.User;
import com.steven.willysbakeshop.model.UserDTO;
import com.steven.willysbakeshop.repository.UserRepository;
import com.steven.willysbakeshop.service.UserService;
import com.steven.willysbakeshop.utilities.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    private UserService userService;

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
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserDTO userDTO)  {
       if (userService.registerNewUserAccount(userDTO)) {
           userDTO.setPassword("");
           return ResponseEntity.ok(userDTO);
       } else {
           return ResponseEntity.unprocessableEntity().body("Creation failed");
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
    public ResponseEntity<User> editUser(@PathVariable long id, @RequestBody @Valid UserDTO userDTO) throws NotFoundException {
       User user = userService.alterUserAccount(userDTO, id);

        return ResponseEntity.ok(user);
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<User> deleteUser(@PathVariable long id) throws NotFoundException {
        Optional<User> user = userRepository.findById(id);
        user.orElseThrow(() -> new NotFoundException(String.format("User: %d could not be located", id)));
        userRepository.delete(user.get());

        return ResponseEntity.ok(user.get());
    }

}
