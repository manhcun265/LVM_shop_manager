package com.shoplvm.productmanagement.repository;

import com.shoplvm.productmanagement.entity.ProductStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductStatusLogRepository extends JpaRepository<ProductStatusLog, Long> {
}