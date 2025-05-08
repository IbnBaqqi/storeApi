package com.salausmart.store.controllers;

import com.salausmart.store.dtos.UserDto;
import com.salausmart.store.mappers.UserMapper;
import com.salausmart.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping
    public Iterable<UserDto> getAllUsers(@RequestParam(required = false, defaultValue = "", name = "sort") String sortBy) {

        if (!Set.of("name", "email").contains(sortBy))
            sortBy = "name";

        return userRepository.findAll(Sort.by(sortBy))
                .stream()
                .map(userMapper::toDto)     // user -> userMapper.toDto(user) using mapStruct
//                .map(user -> new UserDto(user.getId(), user.getName(), user.getEmail())) // manual mapping
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // different ways to return status code
            return ResponseEntity.notFound().build();
        }
        // var userDto = new UserDto(user.getId(), user.getName(), user.getEmail()); //manual mapping
        return ResponseEntity.ok(userMapper.toDto(user));
    }
}
