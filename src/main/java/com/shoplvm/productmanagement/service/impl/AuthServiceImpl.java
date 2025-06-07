package com.shoplvm.productmanagement.service.impl;

import com.shoplvm.productmanagement.dto.request.LoginRequest;
import com.shoplvm.productmanagement.dto.request.RegisterRequest;
import com.shoplvm.productmanagement.dto.response.UserResponse;
import com.shoplvm.productmanagement.entity.User;
import com.shoplvm.productmanagement.repository.AuthRepository;
import com.shoplvm.productmanagement.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse register(RegisterRequest request) {
        log.info("Attempting to register user with email: {}", request.getEmail());

        if (authRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed. Email already exists: {}", request.getEmail());
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        user.setCreatedAt(Timestamp.from(Instant.now()));
        authRepository.save(user);

        log.info("User registered successfully with ID: {}", user.getId());
        return toUserResponse(user);
    }

    @Override
    public UserResponse login(LoginRequest request) {
        log.info("User login attempt with email: {}", request.getEmail());

        User user = authRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed. Email not found: {}", request.getEmail());
                    return new RuntimeException("Invalid email or password");
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            log.warn("Login failed. Incorrect password for email: {}", request.getEmail());
            throw new RuntimeException("Invalid email or password");
        }

        log.info("User logged in successfully: {}", user.getId());
        return toUserResponse(user);
    }

    @Override
    public void logout() {
        log.info("User logged out.");
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }
}