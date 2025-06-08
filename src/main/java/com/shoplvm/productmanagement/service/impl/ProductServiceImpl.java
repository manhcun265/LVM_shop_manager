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

/**
 * Service xử lý các thao tác quản lý sản phẩm.
 * Bao gồm các chức năng CRUD, cập nhật trạng thái và tìm kiếm sản phẩm.
 */
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
     * Tạo mới một sản phẩm trong hệ thống.
     *
     * @param dto Thông tin sản phẩm cần tạo
     * @return ID của sản phẩm vừa được tạo
     * @throws RuntimeException nếu không tìm thấy danh mục hoặc người dùng
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
     * Cập nhật thông tin của một sản phẩm.
     *
     * @param id  ID của sản phẩm cần cập nhật
     * @param dto Thông tin mới của sản phẩm
     * @throws RuntimeException nếu không tìm thấy sản phẩm, danh mục hoặc người dùng
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

        log.info("Cập nhật sản phẩm thành công, productId={}", id);
    }

    /**
     * Xóa một sản phẩm khỏi hệ thống.
     * Tạo bản ghi log trạng thái trước khi xóa.
     *
     * @param id     ID của sản phẩm cần xóa
     * @param userId ID của người dùng thực hiện xóa
     * @throws RuntimeException nếu không tìm thấy sản phẩm hoặc người dùng
     */
    @Override
    public void delete(Long id, Long userId) {
        Product product =
                productRepository
                        .findById(id)
                        .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        ProductStatusLog logEntry = new ProductStatusLog();
        logEntry.setProduct(product);
        logEntry.setUser(user);
        logEntry.setStatus("DELETED");
        logEntry.setLogDate(new Timestamp(System.currentTimeMillis()));
        statusLogRepository.save(logEntry);

        productRepository.deleteById(id);
        log.info("Sản phẩm đã được xóa vĩnh viễn, productId={}", id);
    }

    /**
     * Tìm kiếm sản phẩm theo từ khóa và danh mục.
     *
     * @param keyword    Từ khóa tìm kiếm trong tên sản phẩm
     * @param categoryId ID của danh mục cần lọc
     * @param page       Số trang
     * @param size       Số sản phẩm trên mỗi trang
     * @return Trang kết quả chứa danh sách sản phẩm tìm được
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

    /**
     * Chuyển đổi từ entity Product sang DTO ProductDetailResponse.
     *
     * @param product Entity Product cần chuyển đổi
     * @return Đối tượng ProductDetailResponse đã chuyển đổi
     */
    private ProductDetailResponse convertToProductDetailResponse(Product product) {
        return new ProductDetailResponse(
                product.getId(), product.getName(), product.getPrice(), product.getCategory().getName());
    }

    /**
     * Lấy danh sách sản phẩm theo danh mục có phân trang.
     *
     * @param categoryId ID của danh mục cần lọc
     * @param page       Số trang
     * @param size       Số sản phẩm trên mỗi trang
     * @return Trang kết quả chứa danh sách sản phẩm của danh mục
     */
    public Page<ProductDetailResponse> getProductsByCategory(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findByCategoryId(categoryId, pageable);
        return productPage.map(this::convertToProductDetailResponse);
    }

    /**
     * Cập nhật trạng thái của sản phẩm.
     * Tạo bản ghi log cho việc thay đổi trạng thái.
     *
     * @param request Thông tin yêu cầu cập nhật trạng thái
     * @throws RuntimeException nếu không tìm thấy sản phẩm hoặc người dùng
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

        ProductStatusLog statusLog = new ProductStatusLog();
        statusLog.setProduct(product);
        statusLog.setUser(user);
        statusLog.setStatus(request.getStatus().name());
        statusLog.setLogDate(new Timestamp(System.currentTimeMillis()));
        statusLogRepository.save(statusLog);

        product.setStatus(request.getStatus());
        productRepository.save(product);

        log.info(
                "Đã cập nhật trạng thái sản phẩm productId={}, status={}",
                request.getProductId(),
                request.getStatus());
    }
}