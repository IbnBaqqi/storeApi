package com.salausmart.store.controllers;

import com.salausmart.store.dtos.ChangePasswordRequest;
import com.salausmart.store.dtos.RegisterUserRequest;
import com.salausmart.store.dtos.UpdateUserRequest;
import com.salausmart.store.dtos.UserDto;
import com.salausmart.store.mappers.UserMapper;
import com.salausmart.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody RegisterUserRequest request, UriComponentsBuilder uriBuilder) {
        var user = userMapper.toEntity(request);
        userRepository.save(user);
        var userDto = userMapper.toDto(user);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable(name = "id") Long id, @RequestBody UpdateUserRequest request) {
       var user = userRepository.findById(id).orElse(null);
       if (user == null)
           return ResponseEntity.notFound().build();
       userMapper.update(request, user);
       userRepository.save(user);

       return ResponseEntity.ok(userMapper.toDto(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null)
            return ResponseEntity.notFound().build();

        userRepository.delete(user);
        return ResponseEntity.noContent().build();

    }

//    Using post because it's an action
    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @RequestBody ChangePasswordRequest request) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null)
            return ResponseEntity.notFound().build();
        if (!user.getPassword().equals(request.getOldPassword()))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        user.setPassword(request.getNewPassword());
        userRepository.save(user);

        return ResponseEntity.noContent().build();
    }

}
