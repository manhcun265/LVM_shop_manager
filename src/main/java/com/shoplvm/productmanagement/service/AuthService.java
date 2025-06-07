package com.shoplvm.productmanagement.service;

import com.shoplvm.productmanagement.dto.request.LoginRequest;
import com.shoplvm.productmanagement.dto.request.RegisterRequest;
import com.shoplvm.productmanagement.dto.response.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest request);

    UserResponse login(LoginRequest request);

    void logout();
}
