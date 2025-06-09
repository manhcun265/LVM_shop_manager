package com.shoplvm.productmanagement.controller;

import com.shoplvm.productmanagement.dto.request.LoginRequest;
import com.shoplvm.productmanagement.dto.request.RegisterRequest;
import com.shoplvm.productmanagement.dto.response.LoginResponse;
import com.shoplvm.productmanagement.dto.response.RegisterResponse;
import com.shoplvm.productmanagement.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Controller xử lý các yêu cầu liên quan đến xác thực
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    /**
     * Đăng ký tài khoản mới
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        log.info("Nhận yêu cầu đăng ký từ username={}, email={}", request.getUsername(), request.getEmail());
        return authService.register(request);
    }

    /**
     * Đăng nhập
     */
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        log.info("Nhận yêu cầu đăng nhập từ email={}", request.getEmail());
        return authService.login(request);
    }

    /**
     * Đăng xuất
     */
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout() {
        log.info("Nhận yêu cầu đăng xuất");
        authService.logout();
    }
}
