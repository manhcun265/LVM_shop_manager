package com.shoplvm.productmanagement.service;

import com.shoplvm.productmanagement.dto.request.LoginRequest;
import com.shoplvm.productmanagement.dto.request.RegisterRequest;
import com.shoplvm.productmanagement.dto.response.LoginResponse;
import com.shoplvm.productmanagement.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    void logout(String token);
}
