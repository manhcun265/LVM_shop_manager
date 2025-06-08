package com.shoplvm.productmanagement.repository;

import com.shoplvm.productmanagement.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository xử lý truy vấn dữ liệu sản phẩm
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    /**
     * Tìm kiếm sản phẩm theo tên (không phân biệt hoa thường)
     */
    Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    /**
     * Tìm kiếm sản phẩm theo danh mục
     */
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    /**
     * Tìm kiếm sản phẩm theo tên và danh mục
     */
    Page<Product> findByNameContainingIgnoreCaseAndCategoryId(
            String keyword, Long categoryId, Pageable pageable);
}
