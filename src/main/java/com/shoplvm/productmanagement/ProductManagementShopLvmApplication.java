package com.shoplvm.productmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ProductManagementShopLvmApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductManagementShopLvmApplication.class, args);
    }

}
