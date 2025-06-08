package com.shoplvm.productmanagement.controller;

import com.shoplvm.productmanagement.dto.request.LoginRequest;
import com.shoplvm.productmanagement.dto.request.RegisterRequest;
import com.shoplvm.productmanagement.dto.response.LoginResponse;
import com.shoplvm.productmanagement.dto.response.RegisterResponse;
import com.shoplvm.productmanagement.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller xử lý các yêu cầu liên quan đến xác thực
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Đăng ký tài khoản mới
     */
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse register = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RegisterResponse.builder()
                        .message("Đăng ký tài khoản thành công")
                        .userId(register.getUserId())
                        .build());
    }

    /**
     * Đăng nhập
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse login = authService.login(request);
        return ResponseEntity.ok(LoginResponse.builder()
                .message("Đăng nhập thành công")
                .token(login.getToken())
                .build());
    }

    /**
     * Đăng xuất
     */
    @PostMapping("/logout")
    public ResponseEntity<LoginResponse> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return ResponseEntity.ok(LoginResponse.builder()
                .message("Đăng xuất thành công")
                .build());
    }
}
