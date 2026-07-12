package com.example.student.service;

import com.example.student.dto.LoginRequest;
import com.example.student.dto.LoginResponse;
import com.example.student.dto.RegisterRequest;
import com.example.student.entity.User;
import com.example.student.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;

    public LoginResponse validateUser(LoginRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .map(user -> {
                    if (user.getPassword().equals(request.getPassword())) {
                        return LoginResponse.builder()
                                .status(true)
                                .userExists(true)
                                .message("Login Successful")
                                .username(user.getUsername())
                                .branch(user.getBranch())
                                .build();
                    }
                    return LoginResponse.builder()
                            .status(false)
                            .userExists(true)
                            .message("Invalid username or password")
                            .username(request.getUsername())
                            .build();
                })
                .orElse(LoginResponse.builder()
                        .status(false)
                        .userExists(false)
                        .message("Account not found. Please create account.")
                        .username(request.getUsername())
                        .build());
    }

    public LoginResponse registerUser(RegisterRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()
                || request.getBranch() == null || request.getBranch().isBlank()) {
            return LoginResponse.builder()
                    .status(false)
                    .userExists(false)
                    .message("Email, password, and branch are required.")
                    .username(request.getUsername())
                    .build();
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return LoginResponse.builder()
                    .status(false)
                    .userExists(true)
                    .message("Account already exists. Please login.")
                    .username(request.getUsername())
                    .build();
        }

        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setPassword(request.getPassword());
        user.setBranch(request.getBranch().trim().toUpperCase());
        userRepository.save(user);

        return LoginResponse.builder()
                .status(true)
                .userExists(true)
                .message("Account created successfully")
                .username(user.getUsername())
                .branch(user.getBranch())
                .build();
    }
}
