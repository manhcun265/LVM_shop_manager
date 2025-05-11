package com.shoplvm.productmanagement.service;

import com.shoplvm.productmanagement.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategories();

}
