package com.shoplvm.productmanagement.controller;

import com.shoplvm.productmanagement.dto.request.CategoryRequest;
import com.shoplvm.productmanagement.dto.response.CategoryResponse;
import com.shoplvm.productmanagement.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller xử lý các yêu cầu liên quan đến danh mục sản phẩm
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * Tạo mới danh mục
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse create(@Valid @RequestBody CategoryRequest request) {
        log.info("Nhận yêu cầu tạo danh mục mới: {}", request.getName());
        Long id = categoryService.create(request);
        return CategoryResponse.builder()
                .message("Thêm danh mục thành công")
                .id(id)
                .build();
    }

    /**
     * Cập nhật thông tin danh mục
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponse update(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        log.info("Nhận yêu cầu cập nhật danh mục ID={}", id);
        categoryService.update(id, request);
        return CategoryResponse.builder()
                .message("Cập nhật danh mục thành công")
                .id(id)
                .build();
    }

    /**
     * Xóa danh mục
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        log.info("Nhận yêu cầu xóa danh mục ID={}", id);
        categoryService.delete(id);
    }

    /**
     * Lấy danh sách tất cả danh mục
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryResponse> findAll() {
        log.info("Lấy danh sách tất cả danh mục");
        return categoryService.findAll();
    }

    /**
     * Lấy thông tin chi tiết danh mục theo ID
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponse findById(@PathVariable Long id) {
        log.info("Lấy thông tin chi tiết danh mục ID={}", id);
        return categoryService.findById(id);
    }
}

