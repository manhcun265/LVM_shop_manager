package com.shoplvm.productmanagement.repository;

import com.shoplvm.productmanagement.entity.ProductStatusLog;
import org.springframework.data.repository.CrudRepository;

public interface ProductStatusLogRepository extends CrudRepository<ProductStatusLog, Long> {
}
