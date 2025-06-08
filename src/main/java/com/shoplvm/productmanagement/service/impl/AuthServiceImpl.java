package com.shoplvm.productmanagement.service.impl;

import com.shoplvm.productmanagement.dto.request.LoginRequest;
import com.shoplvm.productmanagement.dto.request.RegisterRequest;
import com.shoplvm.productmanagement.dto.response.LoginResponse;
import com.shoplvm.productmanagement.dto.response.RegisterResponse;
import com.shoplvm.productmanagement.entity.User;
import com.shoplvm.productmanagement.repository.UserRepository;
import com.shoplvm.productmanagement.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * Service implementation xử lý logic nghiệp vụ liên quan đến xác thực
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Đăng ký tài khoản mới
     *
     * @return
     */
    @Override
    public RegisterResponse register(RegisterRequest request) {
        // Kiểm tra username đã tồn tại
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }

        // Kiểm tra email đã tồn tại
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        // Tạo user mới
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        log.info("Đăng ký tài khoản thành công, username={}", request.getUsername());
        return null;
    }

    /**
     * Đăng nhập
     *
     * @return
     */
    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            // Tìm user theo email
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Email hoặc mật khẩu không đúng"));

            // Kiểm tra mật khẩu
            if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
                throw new RuntimeException("Email hoặc mật khẩu không đúng");
            }

            log.info("Đăng nhập thành công, email={}", request.getEmail());
        } catch (Exception e) {
            log.error("Đăng nhập thất bại, email={}, error={}", request.getEmail(), e.getMessage());
            throw new RuntimeException("Email hoặc mật khẩu không đúng");
        }
        // Trả về thông tin đăng nhập thành công
        LoginResponse response = new LoginResponse();
        response.setMessage("Đăng nhập thành công");
        response.setToken("mock-jwt-token"); // Giả lập token JWT
        log.info("Trả về thông tin đăng nhập thành công: {}", response);

        return response;
    }

    /**
     * Đăng xuất
     */
    @Override
    public void logout(String token) {
        log.info("Đăng xuất thành công");
    }
}