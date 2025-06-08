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
     */
    @Override
    public RegisterResponse register(RegisterRequest request) {
        log.info("Bắt đầu đăng ký tài khoản mới với username={}, email={}", request.getUsername(), request.getEmail());

        try {
            // Kiểm tra username đã tồn tại
            if (userRepository.existsByUsername(request.getUsername())) {
                log.error("Đăng ký thất bại: Tên đăng nhập đã tồn tại, username={}", request.getUsername());
                throw new RuntimeException("Tên đăng nhập đã tồn tại");
            }

            // Kiểm tra email đã tồn tại
            if (userRepository.existsByEmail(request.getEmail())) {
                log.error("Đăng ký thất bại: Email đã tồn tại, email={}", request.getEmail());
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

            log.info("Đăng ký tài khoản thành công, userId={}, username={}", user.getId(), user.getUsername());

            return RegisterResponse.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .message("Đăng ký tài khoản thành công")
                    .build();
                    
        } catch (Exception e) {
            log.error("Lỗi khi đăng ký tài khoản: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Đăng nhập
     */
    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("Bắt đầu đăng nhập với email={}", request.getEmail());
        try {
            // Tìm user theo email
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Email hoặc mật khẩu không đúng"));

            // Kiểm tra mật khẩu
            if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
                log.error("Đăng nhập thất bại: Mật khẩu không đúng, email={}", request.getEmail());
                throw new RuntimeException("Email hoặc mật khẩu không đúng");
            }

            log.info("Đăng nhập thành công, userId={}, email={}", user.getId(), request.getEmail());
            
            // Trả về thông tin đăng nhập thành công
            return LoginResponse.builder()
                    .token("mock-jwt-token") // Giả lập token JWT
                    .email(user.getEmail())
                    .message("Đăng nhập thành công")
                    .build();
                    
        } catch (Exception e) {
            log.error("Lỗi khi đăng nhập: {}", e.getMessage(), e);
            throw new RuntimeException("Email hoặc mật khẩu không đúng");
        }
    }

    /**
     * Đăng xuất
     */
    @Override
    public void logout() {
        log.info("Đăng xuất thành công");
    }
}