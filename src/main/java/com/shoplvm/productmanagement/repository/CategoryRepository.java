package com.shoplvm.productmanagement.repository;

import com.shoplvm.productmanagement.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository xử lý truy vấn dữ liệu danh mục sản phẩm
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
