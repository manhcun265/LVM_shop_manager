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
     * Chuyển đổi từ danh sách User entity sang danh sách UserResponse
     * 
     * @return List<UserResponse> Danh sách thông tin người dùng
     */
    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lấy danh sách người dùng có phân trang
     * 
     * @param page Số trang (bắt đầu từ 0)
     * @param size Số lượng phần tử trên mỗi trang
     * @return Page<UserResponse> Trang dữ liệu chứa thông tin người dùng
     */
    @Override
    public Page<UserResponse> getUsersWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable)
                .map(this::toUserResponse);
    }

    /**
     * Lấy thông tin chi tiết người dùng theo ID
     * 
     * @param id ID của người dùng cần lấy thông tin
     * @return UserResponse Thông tin chi tiết người dùng
     * @throws IllegalArgumentException Nếu không tìm thấy người dùng
     */
    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));
        return toUserResponse(user);
    }

    /**
     * Cập nhật thông tin người dùng
     * Kiểm tra username và email có bị trùng không trước khi cập nhật
     * 
     * @param id ID của người dùng cần cập nhật
     * @param request Thông tin cập nhật mới
     * @throws IllegalArgumentException Nếu không tìm thấy người dùng hoặc thông tin bị trùng
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
     * Không cho phép xóa tài khoản admin
     * 
     * @param id ID của người dùng cần xóa
     * @throws IllegalArgumentException Nếu không tìm thấy người dùng hoặc là tài khoản admin
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

    /**
     * Cập nhật vai trò người dùng
     * Chỉ cho phép cập nhật thành USER hoặc ADMIN
     * 
     * @param id ID của người dùng cần cập nhật vai trò
     * @param role Vai trò mới (USER/ADMIN)
     * @throws IllegalArgumentException Nếu không tìm thấy người dùng hoặc vai trò không hợp lệ
     */
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
     * 
     * @param user Entity User chứa thông tin người dùng từ database
     * @return UserResponse chứa thông tin người dùng đã được chuyển đổi
     *         - id: ID của người dùng
     *         - username: Tên đăng nhập
     *         - email: Địa chỉ email
     *         - role: Vai trò (USER/ADMIN)
     *         - message: Thông báo kết quả thao tác
     */
    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())           // Lấy ID từ entity User
                .username(user.getUsername()) // Lấy tên đăng nhập
                .email(user.getEmail())     // Lấy địa chỉ email
                .role(user.getRole())       // Lấy vai trò người dùng
                .message("Thao tác thành công") // Thêm thông báo mặc định
                .build();
    }
}