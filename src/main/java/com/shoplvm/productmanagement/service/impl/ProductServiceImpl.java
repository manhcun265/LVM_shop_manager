package com.shoplvm.productmanagement.service.impl;

import com.shoplvm.productmanagement.dto.request.ProductRequest;
import com.shoplvm.productmanagement.dto.request.UpdateProductStatusRequest;
import com.shoplvm.productmanagement.dto.response.ProductDetailResponse;
import com.shoplvm.productmanagement.entity.*;
import com.shoplvm.productmanagement.entity.Category;
import com.shoplvm.productmanagement.repository.*;
import com.shoplvm.productmanagement.service.ProductService;
import java.sql.Timestamp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository imageRepository;
    private final UserRepository userRepository;
    private final ProductStatusLogRepository statusLogRepository;

    /**
     * create new product to DB
     *
     * @param dto
     * @return productId
     */
    @Override
    public Long create(ProductRequest dto) {
        Category category =
                categoryRepository
                        .findById(dto.getCategoryId())
                        .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));

        User user =
                userRepository
                        .findById(dto.getUserId())
                        .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setCategory(category);
        product.setDetail(dto.getDetail() != null ? dto.getDetail().toString() : null);
        product.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        product.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        productRepository.save(product);

        for (String url : dto.getImageUrls()) {
            ProductImage image = new ProductImage();
            image.setProduct(product);
            image.setImageUrl(url);
            imageRepository.save(image);
        }

        ProductStatusLog statusLog = new ProductStatusLog();
        statusLog.setProduct(product);
        statusLog.setUser(user);
        statusLog.setStatus("CREATED");
        statusLog.setLogDate(new Timestamp(System.currentTimeMillis()));
        statusLogRepository.save(statusLog);

        log.info("Thêm sản phẩm thành công, productId={}", product.getId());
        return product.getId();
    }

    /**
     * update product to DB
     *
     * @param id
     * @param dto
     */
    @Override
    public void update(Long id, ProductRequest dto) {
        Product product =
                productRepository
                        .findById(id)
                        .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        Category category =
                categoryRepository
                        .findById(dto.getCategoryId())
                        .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));

        User user =
                userRepository
                        .findById(dto.getUserId())
                        .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setCategory(category);
        product.setDetail(dto.getDetail() != null ? dto.getDetail().toString() : null);
        //        product.setUpdatedAt(new Timestamp(System.currentTimeMillis())); // bỏ đoạn này vì đã
        // sử dụng @UpdateTimestamp
        productRepository.save(product);

        for (String url : dto.getImageUrls()) {
            ProductImage image = new ProductImage();
            image.setProduct(product);
            image.setImageUrl(url);
            imageRepository.save(image);
        }

        ProductStatusLog statusLog = new ProductStatusLog();
        statusLog.setProduct(product);
        statusLog.setUser(user);
        statusLog.setStatus("UPDATED");
        statusLog.setLogDate(new Timestamp(System.currentTimeMillis()));
        statusLogRepository.save(statusLog);

        log.info("Cập nhật sản phẩm thành công, productId={}", product.getId());
    }

    /**
     * delete product to DB
     *
     * @param id
     * @param userId
     */
    @Override
    public void delete(Long id, Long userId) {
        // 1. Kiểm tra tồn tại
        Product product =
                productRepository
                        .findById(id)
                        .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));


        // 2. Ghi log trạng thái trước khi xóa
        ProductStatusLog logEntry = new ProductStatusLog();
        logEntry.setProduct(product);
        logEntry.setStatus("DELETED");
        logEntry.setLogDate(new Timestamp(System.currentTimeMillis()));
        statusLogRepository.save(logEntry);

        // 3. Xóa sản phẩm
        productRepository.deleteById(id);

        log.info("Sản phẩm đã được xóa vĩnh viễn, productId={}", id);
    }

    /**
     * search product by keyword and categoryId
     *
     * @param keyword
     * @param categoryId
     * @param page
     * @param size
     * @return Page<ProductResponse>
     */
    public Page<ProductDetailResponse> searchProducts(
            String keyword, Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage;

        if (keyword != null && categoryId != null) {
            productPage =
                    productRepository.findByNameContainingIgnoreCaseAndCategoryId(
                            keyword, categoryId, pageable);
        } else if (keyword != null) {
            productPage = productRepository.findByNameContainingIgnoreCase(keyword, pageable);
        } else if (categoryId != null) {
            productPage = productRepository.findByCategoryId(categoryId, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        return productPage.map(this::convertToProductDetailResponse);
    }

    private ProductDetailResponse convertToProductDetailResponse(Product product) {
        return new ProductDetailResponse(
                product.getId(), product.getName(), product.getPrice(), product.getCategory().getName());
    }

    /**
     * get products by categoryId
     *
     * @param categoryId
     * @param page
     * @param size
     * @return Page<ProductResponse>
     */
    public Page<ProductDetailResponse> getProductsByCategory(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findByCategoryId(categoryId, pageable);
        return productPage.map(this::convertToProductDetailResponse);
    }

    /**
     * update product status
     *
     * @param request
     */
    @Override
    public void updateProductStatus(UpdateProductStatusRequest request) {
        Product product =
                productRepository
                        .findById(request.getProductId())
                        .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        User user =
                userRepository
                        .findById(request.getUserId())
                        .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        // Ghi log trước khi cập nhật
        ProductStatusLog statusLog = new ProductStatusLog();
        statusLog.setProduct(product);
        statusLog.setUser(user);
        statusLog.setStatus(request.getStatus().name());
        statusLog.setLogDate(new Timestamp(System.currentTimeMillis()));
        statusLogRepository.save(statusLog);

        // Cập nhật trạng thái
        product.setStatus(request.getStatus());
        productRepository.save(product);

        log.info(
                "Đã cập nhật trạng thái sản phẩm productId={}, status={}",
                request.getProductId(),
                request.getStatus());
    }

    @Override
    public Page<ProductDetailResponse> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(this::convertToProductDetailResponse);
    }
}