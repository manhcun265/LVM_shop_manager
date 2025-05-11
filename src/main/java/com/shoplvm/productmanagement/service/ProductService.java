package com.shoplvm.productmanagement.service;

import com.shoplvm.productmanagement.dto.request.ProductRequest;
import com.shoplvm.productmanagement.dto.response.ProductDetailResponse;
import org.springframework.data.domain.Page;

public interface ProductService {
    Long create(ProductRequest dto);

    void update(Long id, ProductRequest dto);

    void delete(Long id, Long userId);

    Page<ProductDetailResponse> searchProducts(String keyword, Long categoryId, int page, int size);

    Page<ProductDetailResponse> getProductsByCategory(Long categoryId, int page, int size);
}