package com.shoplvm.productmanagement.controller;

import com.shoplvm.productmanagement.dto.request.UpdateUserRequest;
import com.shoplvm.productmanagement.dto.request.UpdateUserRoleRequest;
import com.shoplvm.productmanagement.dto.response.UserResponse;
import com.shoplvm.productmanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller xử lý các yêu cầu liên quan đến người dùng
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * Lấy danh sách tất cả người dùng
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getAllUsers() {
        log.info("Lấy danh sách tất cả người dùng");
        return userService.getAllUsers();
    }

    /**
     * Lấy thông tin chi tiết người dùng theo ID
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUserById(@PathVariable Long id) {
        log.info("Lấy thông tin chi tiết người dùng ID={}", id);
        return userService.getUserById(id);
    }

    /**
     * Cập nhật thông tin người dùng
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest dto) {
        log.info("Nhận yêu cầu cập nhật thông tin người dùng ID={}", id);
        userService.updateUser(id, dto);
        return userService.getUserById(id);
    }

    /**
     * Xóa người dùng
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        log.info("Nhận yêu cầu xóa người dùng ID={}", id);
        userService.deleteUser(id);
    }

    /**
     * Cập nhật vai trò người dùng
     */
    @PutMapping("/{id}/role")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse updateUserRole(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRoleRequest request) {
        log.info("Nhận yêu cầu cập nhật vai trò người dùng ID={} thành {}", id, request.getRole());
        userService.updateUserRole(id, request.getRole());
        return userService.getUserById(id);
    }
}