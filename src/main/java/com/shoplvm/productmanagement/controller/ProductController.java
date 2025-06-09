package com.shoplvm.productmanagement.controller;

import com.shoplvm.productmanagement.dto.request.ProductRequest;
import com.shoplvm.productmanagement.dto.request.UpdateProductStatusRequest;
import com.shoplvm.productmanagement.dto.response.ProductDetailResponse;
import com.shoplvm.productmanagement.dto.response.ProductResponse;
import com.shoplvm.productmanagement.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Controller xử lý các yêu cầu liên quan đến sản phẩm
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;

    /**
     * Tạo mới sản phẩm
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@Valid @RequestBody ProductRequest dto) {
        log.info("Nhận yêu cầu tạo sản phẩm mới: {}", dto.getName());
        Long id = productService.create(dto);
        return ProductResponse.builder()
                .message("Thêm sản phẩm thành công")
                .productId(id)
                .build();
    }

    /**
     * Cập nhật thông tin sản phẩm
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest dto) {
        log.info("Nhận yêu cầu cập nhật sản phẩm ID={}", id);
        productService.update(id, dto);
        return ProductResponse.builder()
                .message("Cập nhật sản phẩm thành công")
                .productId(id)
                .build();
    }

    /**
     * Xóa sản phẩm
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(
            @PathVariable Long id,
            @RequestParam Long userId) {
        log.info("Nhận yêu cầu xóa sản phẩm ID={} bởi user ID={}", id, userId);
        productService.delete(id, userId);
    }

    /**
     * Tìm kiếm sản phẩm theo từ khóa và danh mục
     */
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Page<ProductDetailResponse> searchProducts(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Tìm kiếm sản phẩm với keyword={}, categoryId={}, page={}, size={}", 
                keyword, categoryId, page, size);
        return productService.searchProducts(keyword, categoryId, page, size);
    }

    /**
     * Lấy danh sách sản phẩm theo danh mục
     */
    @GetMapping("/category/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public Page<ProductDetailResponse> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Lấy danh sách sản phẩm theo danh mục ID={}, page={}, size={}", 
                categoryId, page, size);
        return productService.getProductsByCategory(categoryId, page, size);
    }

    /**
     * Cập nhật trạng thái sản phẩm
     */
    @PutMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse updateProductStatus(
            @Valid @RequestBody UpdateProductStatusRequest request) {
        log.info("Cập nhật trạng thái sản phẩm ID={} thành {}", 
                request.getProductId(), request.getStatus());
        productService.updateProductStatus(request);
        return ProductResponse.builder()
                .message("Cập nhật trạng thái sản phẩm thành công")
                .productId(request.getProductId())
                .build();
    }
}
