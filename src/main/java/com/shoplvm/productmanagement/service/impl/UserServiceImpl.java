package com.shoplvm.productmanagement.service.impl;

import com.shoplvm.productmanagement.dto.request.UpdateUserRequest;
import com.shoplvm.productmanagement.dto.response.UserResponse;
import com.shoplvm.productmanagement.entity.User;
import com.shoplvm.productmanagement.repository.UserRepository;
import com.shoplvm.productmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation xử lý logic nghiệp vụ liên quan đến người dùng
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Lấy danh sách tất cả người dùng
     */
    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lấy danh sách người dùng có phân trang
     */
    @Override
    public Page<UserResponse> getUsersWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable)
                .map(this::toUserResponse);
    }

    /**
     * Lấy thông tin chi tiết người dùng theo ID
     */
    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));
        return toUserResponse(user);
    }

    /**
     * Cập nhật thông tin người dùng
     *
     * @return
     */
    @Override
    @Transactional
    public void updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));

        // Kiểm tra username đã tồn tại
        if (!user.getUsername().equals(request.getUsername()) 
                && userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại");
        }

        // Kiểm tra email đã tồn tại
        if (!user.getEmail().equals(request.getEmail()) 
                && userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        userRepository.save(user);
        log.info("Cập nhật thông tin người dùng thành công, userId={}", id);
    }

    /**
     * Xóa người dùng
     */
    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));

        // Không cho phép xóa tài khoản admin
        if (user.getRole().equals("ADMIN")) {
            throw new IllegalArgumentException("Không thể xóa tài khoản admin");
        }

        userRepository.deleteById(id);
        log.info("Xóa người dùng thành công, userId={}", id);
    }

    @Override
    public void updateUserRole(Long id, String role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));

        // Kiểm tra role hợp lệ
        if (!role.equals("USER") && !role.equals("ADMIN")) {
            throw new IllegalArgumentException("Vai trò không hợp lệ");
        }

        user.setRole(role);
        userRepository.save(user);
        log.info("Cập nhật vai trò người dùng thành công, userId={}, role={}", id, role);
    }

    /**
     * Chuyển đổi từ User entity sang UserResponse
     */
    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}