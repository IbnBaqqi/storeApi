package com.salausmart.store.controllers;

import com.salausmart.store.dtos.UserDto;
import com.salausmart.store.mappers.UserMapper;
import com.salausmart.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@AllArgsConstructor
//@RestController("/users")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping
    public Iterable<UserDto> getAllusers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)// user -> userMapper.toDto(user) using mapStruct
//                .map(user -> new UserDto(user.getId(), user.getName(), user.getEmail())) // manual mapping
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
//            different ways to return status code
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return ResponseEntity.notFound().build();
        }
//        return new ResponseEntity<>(user, HttpStatus.OK);
        // var userDto = new UserDto(user.getId(), user.getName(), user.getEmail()); manual mapping
        return ResponseEntity.ok(userMapper.toDto(user));
    }
}
