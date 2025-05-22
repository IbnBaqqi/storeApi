package com.salausmart.store.auth;

import com.salausmart.store.users.User;
import com.salausmart.store.users.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (long) authentication.getPrincipal();

        return userRepository.findById(userId).orElse(null);
    }
}
