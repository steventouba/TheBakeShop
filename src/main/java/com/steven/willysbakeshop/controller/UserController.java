package com.steven.willysbakeshop.controller;

import com.steven.willysbakeshop.model.UserRequestDTO;
import com.steven.willysbakeshop.model.UserResponseDTO;
import com.steven.willysbakeshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Transactional
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("OK");
    }

    @GetMapping(value = "/")
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        List<UserResponseDTO> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable long id) {
        UserResponseDTO user = userService.findById(id);

        return ResponseEntity.ok(user);
    }

    @PutMapping(value = "/{id}/edit")
    public ResponseEntity<UserResponseDTO> editUser(@PathVariable long id, @RequestBody UserRequestDTO userDTO) {
        UserResponseDTO alteredUser = userService.alterUserAccount(userDTO, id);

        return ResponseEntity.ok(alteredUser);
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        userService.deleteUser(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
