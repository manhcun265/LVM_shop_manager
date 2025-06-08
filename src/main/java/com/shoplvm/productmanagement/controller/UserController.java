package com.shoplvm.productmanagement.controller;

import com.shoplvm.productmanagement.dto.request.UpdateUserRequest;
import com.shoplvm.productmanagement.dto.request.UpdateUserRoleRequest;
import com.shoplvm.productmanagement.dto.response.UserResponse;
import com.shoplvm.productmanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller xử lý các yêu cầu liên quan đến người dùng
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Lấy danh sách tất cả người dùng
     */
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Lấy thông tin chi tiết người dùng theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * Cập nhật thông tin người dùng
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id, @Valid @RequestBody UpdateUserRequest dto) {
       userService.updateUser(id, dto);
        return ResponseEntity.ok(UserResponse.builder()
                .message("Cập nhật thông tin người dùng thành công")
                .id(id)
                .build());
    }

    /**
     * Xóa người dùng
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(UserResponse.builder()
                .message("Xóa người dùng thành công")
                .id(id)
                .build());
    }

    /**
     * Cập nhật vai trò người dùng
     */
    @PutMapping("/{id}/role")
    public ResponseEntity<UserResponse> updateUserRole(
            @PathVariable Long id, @Valid @RequestBody UpdateUserRoleRequest request) {
        userService.updateUserRole(id, request.getRole());
        return ResponseEntity.ok(UserResponse.builder()
                .message("Cập nhật vai trò người dùng thành công")
                .id(id)
                .build());
    }
}