package com.shoplvm.productmanagement.controller;

import com.shoplvm.productmanagement.dto.request.ProductRequest;
import com.shoplvm.productmanagement.dto.request.UpdateProductStatusRequest;
import com.shoplvm.productmanagement.dto.response.ProductDetailResponse;
import com.shoplvm.productmanagement.dto.response.ProductResponse;
import com.shoplvm.productmanagement.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller xử lý các yêu cầu liên quan đến sản phẩm
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    /**
     * Tạo mới sản phẩm
     */
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest dto) {
        Long id = productService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ProductResponse.builder()
                        .message("Thêm sản phẩm thành công")
                        .productId(id)
                        .build());
    }

    /**
     * Cập nhật thông tin sản phẩm
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest dto) {
        productService.update(id, dto);
        return ResponseEntity.ok(ProductResponse.builder()
                .message("Cập nhật sản phẩm thành công")
                .productId(id)
                .build());
    }

    /**
     * Xóa sản phẩm
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponse> deleteProduct(
            @PathVariable Long id,
            @RequestParam Long userId) {
        productService.delete(id, userId);
        return ResponseEntity.ok(ProductResponse.builder()
                .message("Xóa sản phẩm thành công")
                .productId(id)
                .build());
    }

    /**
     * Tìm kiếm sản phẩm theo từ khóa và danh mục
     */
    @GetMapping("/search")
    public ResponseEntity<Page<ProductDetailResponse>> searchProducts(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(productService.searchProducts(keyword, categoryId, page, size));
    }

    /**
     * Lấy danh sách sản phẩm theo danh mục
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductDetailResponse>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId, page, size));
    }

    /**
     * Cập nhật trạng thái sản phẩm
     */
    @PutMapping("/status")
    public ResponseEntity<ProductResponse> updateProductStatus(
            @Valid @RequestBody UpdateProductStatusRequest request) {
        productService.updateProductStatus(request);
        return ResponseEntity.ok(ProductResponse.builder()
                .message("Cập nhật trạng thái sản phẩm thành công")
                .productId(request.getProductId())
                .build());
    }
}
