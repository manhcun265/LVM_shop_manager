package com.shoplvm.productmanagement.controller;

import com.shoplvm.productmanagement.dto.request.ProductRequest;
import com.shoplvm.productmanagement.dto.response.ProductDetailResponse;
import com.shoplvm.productmanagement.dto.response.ProductResponse;
import com.shoplvm.productmanagement.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest dto) {
        Long id = productService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ProductResponse.builder()
                                .message("Thêm sản phẩm thành công")
                                .productId(id)
                                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id, @Valid @RequestBody ProductRequest dto) {
        productService.update(id, dto);
        return ResponseEntity.ok(
                ProductResponse.builder()
                        .message("Cập nhật sản phẩm thành công")
                        .productId(id)
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponse> deleteProduct(
            @PathVariable Long id, @RequestParam Long userId) {
        productService.delete(id, userId);
        return ResponseEntity.ok(
                ProductResponse.builder()
                        .message("Xóa sản phẩm thành công")
                        .productId(id)
                        .build());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductDetailResponse>> searchProducts(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<ProductDetailResponse> products = productService.searchProducts(keyword, categoryId, page, size);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductDetailResponse>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<ProductDetailResponse> products = productService.getProductsByCategory(categoryId, page, size);
        return ResponseEntity.ok(products);
    }


}
