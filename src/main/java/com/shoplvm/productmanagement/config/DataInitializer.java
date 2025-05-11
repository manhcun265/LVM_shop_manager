package com.shoplvm.productmanagement.config;

import com.shoplvm.productmanagement.entity.Category;
import com.shoplvm.productmanagement.entity.User;
import com.shoplvm.productmanagement.repository.CategoryRepository;
import com.shoplvm.productmanagement.repository.UserRepository;

import java.sql.Timestamp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) {
        // Nếu chưa có category thì tạo mới
        if (categoryRepository.count() == 0) {
            Category category = new Category();
            category.setName("MobilePhones");
            categoryRepository.save(category);
            log.info("🌱 Tạo dữ liệu mẫu: Category 'MobilePhones'");
        }

        // Nếu chưa có user thì tạo mới
        if (userRepository.count() == 0) {
            User user = new User();
            user.setUsername("admin");
            user.setEmail("admin@example.com");
            user.setPasswordHash("admin123"); // chỉ để test, không cần mã hoá
            user.setRole("ADMIN");
            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            userRepository.save(user);
            log.info("🌱 Tạo dữ liệu mẫu: User 'admin'");
        }
    }
}
