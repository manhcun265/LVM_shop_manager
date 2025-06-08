package com.shoplvm.productmanagement.repository;

import com.shoplvm.productmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository xử lý truy vấn dữ liệu người dùng
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    /**
     * Tìm người dùng theo email
     */
    Optional<User> findByEmail(String email);

    /**
     * Kiểm tra tên đăng nhập đã tồn tại chưa
     */
    boolean existsByUsername(String username);

    /**
     * Kiểm tra email đã tồn tại chưa
     */
    boolean existsByEmail(String email);
    /**
     * Tìm người dùng theo tên đăng nhập
     */

    Optional<User> findByUsername(String username);
}