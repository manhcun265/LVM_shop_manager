package com.shoplvm.productmanagement.repository;

import com.shoplvm.productmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
