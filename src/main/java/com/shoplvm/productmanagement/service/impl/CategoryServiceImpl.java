package com.shoplvm.productmanagement.service.impl;

import com.shoplvm.productmanagement.dto.request.CategoryRequest;
import com.shoplvm.productmanagement.dto.response.CategoryResponse;
import com.shoplvm.productmanagement.entity.Category;
import com.shoplvm.productmanagement.repository.CategoryRepository;
import com.shoplvm.productmanagement.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation xử lý logic nghiệp vụ liên quan đến danh mục sản phẩm
 * Bao gồm các chức năng:
 * - Tạo mới danh mục
 * - Cập nhật thông tin danh mục
 * - Xóa danh mục
 * - Lấy danh sách danh mục
 * - Lấy thông tin chi tiết danh mục
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Lấy danh sách tất cả danh mục
     * Chuyển đổi từ danh sách Category entity sang danh sách CategoryResponse
     * 
     * @return List<CategoryResponse> Danh sách thông tin danh mục
     */
    @Override
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream()
                .map(this::toCategoryResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lấy thông tin chi tiết danh mục theo ID
     * 
     * @param id ID của danh mục cần lấy thông tin
     * @return CategoryResponse Thông tin chi tiết danh mục
     * @throws IllegalArgumentException Nếu không tìm thấy danh mục
     */
    @Override
    public CategoryResponse findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Danh mục không tồn tại"));
        return toCategoryResponse(category);
    }

    /**
     * Tạo mới danh mục
     * 
     * @param request Thông tin danh mục cần tạo
     * @return Long ID của danh mục vừa tạo
     */
    @Override
    @Transactional
    public Long create(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        categoryRepository.save(category);
        log.info("Tạo mới danh mục thành công, categoryId={}", category.getId());
        return category.getId();
    }

    /**
     * Cập nhật thông tin danh mục
     * 
     * @param id ID của danh mục cần cập nhật
     * @param request Thông tin cập nhật mới
     * @throws IllegalArgumentException Nếu không tìm thấy danh mục
     */
    @Override
    @Transactional
    public void update(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Danh mục không tồn tại"));

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        categoryRepository.save(category);
        log.info("Cập nhật danh mục thành công, categoryId={}", id);
    }

    /**
     * Xóa danh mục
     * 
     * @param id ID của danh mục cần xóa
     * @throws IllegalArgumentException Nếu không tìm thấy danh mục
     */
    @Override
    @Transactional
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Danh mục không tồn tại");
        }
        categoryRepository.deleteById(id);
        log.info("Xóa danh mục thành công, categoryId={}", id);
    }

    /**
     * Chuyển đổi từ Category entity sang CategoryResponse
     * 
     * @param category Entity Category cần chuyển đổi
     * @return CategoryResponse Chứa thông tin danh mục
     */
    private CategoryResponse toCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .build();
    }
}

