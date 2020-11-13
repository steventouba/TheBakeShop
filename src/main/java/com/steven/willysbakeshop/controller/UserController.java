package com.steven.willysbakeshop.controller;

import com.steven.willysbakeshop.model.User;
import com.steven.willysbakeshop.model.UserDTO;
import com.steven.willysbakeshop.repository.UserRepository;
import com.steven.willysbakeshop.service.UserService;
import com.steven.willysbakeshop.util.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Transactional
@CrossOrigin
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
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable long id) {
        UserDTO user = userService.findById(id);

        return ResponseEntity.ok(user);
    }

    @PutMapping(value = "/{id}/edit")
    public ResponseEntity<UserDTO> editUser(@PathVariable long id, @RequestBody UserDTO userDTO) {
       UserDTO alteredUser = userService.alterUserAccount(userDTO, id);

        return ResponseEntity.ok(alteredUser);
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<User> deleteUser(@PathVariable long id) {
        Optional<User> user = userRepository.findById(id);
        user.orElseThrow(() -> new NotFoundException(String.format("User: %d could not be located", id)));
        userRepository.delete(user.get());

        return ResponseEntity.ok(user.get());
    }

//    @PostMapping(value = "/create", consumes= TEXT_CSV_VALUE)
//    public ResponseEntity<String> createProducts(@RequestBody String csv) throws IOException {
//
//        MappingIterator<User> iterator = new CsvMapper()
//                .readerFor(User.class)
//                .with(CsvSchema.emptySchema().withHeader())
//                .readValues(csv);
//
//        while (iterator.hasNext()) {
//            userRepository.save(iterator.next());
//        }
//
//        return ResponseEntity.ok("OK");
//    }

}
