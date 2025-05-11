package com.shoplvm.productmanagement.repository;

import com.shoplvm.productmanagement.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}