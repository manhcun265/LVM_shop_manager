package com.shoplvm.productmanagement.service;

import com.shoplvm.productmanagement.dto.request.CategoryRequest;
import com.shoplvm.productmanagement.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    Long create(CategoryRequest request);

    void update(Long id, CategoryRequest request);

    void delete(Long id);

    List<CategoryResponse> findAll();

    CategoryResponse findById(Long id);
}
