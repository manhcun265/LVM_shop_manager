package com.shoplvm.productmanagement.service;

import com.shoplvm.productmanagement.dto.request.CategoryRequest;
import com.shoplvm.productmanagement.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse create(CategoryRequest request);

    CategoryResponse update(Long id, CategoryRequest request);

    void delete(Long id);

    List<CategoryResponse> findAll();

    CategoryResponse findById(Long id);
}
