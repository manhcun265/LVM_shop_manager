package com.shoplvm.productmanagement.service;

import com.shoplvm.productmanagement.dto.request.UpdateUserRequest;
import com.shoplvm.productmanagement.dto.response.UserResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();

    Page<UserResponse> getUsersWithPagination(int page, int size);

    UserResponse getUserById(Long id);

    void updateUser(Long id, UpdateUserRequest request);

    void deleteUser(Long id);

    void updateUserRole(Long id, String role);
}
