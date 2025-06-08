package com.shoplvm.productmanagement.controller;

import com.shoplvm.productmanagement.dto.request.CategoryRequest;
import com.shoplvm.productmanagement.dto.response.CategoryResponse;
import com.shoplvm.productmanagement.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller xử lý các yêu cầu liên quan đến danh mục sản phẩm
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * Tạo mới danh mục
     */
    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest request) {
        Long id = categoryService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CategoryResponse.builder()
                        .message("Thêm danh mục thành công")
                        .id(id)
                        .build());
    }

    /**
     * Cập nhật thông tin danh mục
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        categoryService.update(id, request);
        return ResponseEntity.ok(CategoryResponse.builder()
                .message("Cập nhật danh mục thành công")
                .id(id)
                .build());
    }

    /**
     * Xóa danh mục
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryResponse> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok(CategoryResponse.builder()
                .message("Xóa danh mục thành công")
                .id(id)
                .build());
    }

    /**
     * Lấy danh sách tất cả danh mục
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    /**
     * Lấy thông tin chi tiết danh mục theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }
}

