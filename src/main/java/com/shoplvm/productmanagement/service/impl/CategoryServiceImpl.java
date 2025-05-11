package com.shoplvm.productmanagement.service.impl;

import com.shoplvm.productmanagement.dto.response.CategoryResponse;
import com.shoplvm.productmanagement.repository.CategoryRepository;
import com.shoplvm.productmanagement.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Get all categories from DB
     *
     * @return List<CategoryResponse>
     */

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(category -> new CategoryResponse(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }
}

