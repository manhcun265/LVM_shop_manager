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
 * Service implementation xử lý logic nghiệp vụ liên quan đến sản phẩm
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
     * Tạo mới sản phẩm
     * 
     * @param dto Thông tin sản phẩm cần tạo
     * @return Long ID của sản phẩm vừa tạo
     * @throws RuntimeException Nếu danh mục hoặc người dùng không tồn tại
     */
    @Override
    public Long create(ProductRequest dto) {
        // Kiểm tra và lấy thông tin danh mục
        Category category =
                categoryRepository
                        .findById(dto.getCategoryId())
                        .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));

        // Kiểm tra và lấy thông tin người dùng
        User user =
                userRepository
                        .findById(dto.getUserId())
                        .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        // Tạo sản phẩm mới
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setCategory(category);
        product.setDetail(dto.getDetail() != null ? dto.getDetail().toString() : null);
        product.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        product.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        productRepository.save(product);

        // Lưu danh sách hình ảnh sản phẩm
        for (String url : dto.getImageUrls()) {
            ProductImage image = new ProductImage();
            image.setProduct(product);
            image.setImageUrl(url);
            imageRepository.save(image);
        }

        // Ghi log trạng thái sản phẩm
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
     * Cập nhật thông tin sản phẩm
     * 
     * @param id ID của sản phẩm cần cập nhật
     * @param dto Thông tin cập nhật mới
     * @throws RuntimeException Nếu sản phẩm, danh mục hoặc người dùng không tồn tại
     */
    @Override
    public void update(Long id, ProductRequest dto) {
        // Kiểm tra và lấy thông tin sản phẩm
        Product product =
                productRepository
                        .findById(id)
                        .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        // Kiểm tra và lấy thông tin danh mục
        Category category =
                categoryRepository
                        .findById(dto.getCategoryId())
                        .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));

        // Kiểm tra và lấy thông tin người dùng
        User user =
                userRepository
                        .findById(dto.getUserId())
                        .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        // Cập nhật thông tin sản phẩm
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setCategory(category);
        product.setDetail(dto.getDetail() != null ? dto.getDetail().toString() : null);
        productRepository.save(product);

        // Cập nhật danh sách hình ảnh
        for (String url : dto.getImageUrls()) {
            ProductImage image = new ProductImage();
            image.setProduct(product);
            image.setImageUrl(url);
            imageRepository.save(image);
        }

        // Ghi log trạng thái cập nhật
        ProductStatusLog statusLog = new ProductStatusLog();
        statusLog.setProduct(product);
        statusLog.setUser(user);
        statusLog.setStatus("UPDATED");
        statusLog.setLogDate(new Timestamp(System.currentTimeMillis()));
        statusLogRepository.save(statusLog);

        log.info("Cập nhật sản phẩm thành công, productId={}", product.getId());
    }

    /**
     * Xóa sản phẩm
     * 
     * @param id ID của sản phẩm cần xóa
     * @param userId ID của người dùng thực hiện xóa
     * @throws RuntimeException Nếu sản phẩm hoặc người dùng không tồn tại
     */
    @Override
    public void delete(Long id, Long userId) {
        // 1. Kiểm tra tồn tại sản phẩm
        Product product =
                productRepository
                        .findById(id)
                        .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        // 2. Kiểm tra tồn tại user
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        // 3. Ghi log trạng thái trước khi xóa
        ProductStatusLog logEntry = new ProductStatusLog();
        logEntry.setProduct(product);
        logEntry.setUser(user);
        logEntry.setStatus("DELETED");
        logEntry.setLogDate(new Timestamp(System.currentTimeMillis()));
        statusLogRepository.save(logEntry);

        // 4. Xóa sản phẩm
        productRepository.deleteById(id);

        log.info("Sản phẩm đã được xóa vĩnh viễn bởi user ID={}, productId={}", userId, id);
    }

    /**
     * Tìm kiếm sản phẩm theo từ khóa và danh mục
     * 
     * @param keyword Từ khóa tìm kiếm (tên sản phẩm)
     * @param categoryId ID danh mục (có thể null)
     * @param page Số trang (bắt đầu từ 0)
     * @param size Số lượng phần tử trên mỗi trang
     * @return Page<ProductDetailResponse> Trang dữ liệu chứa thông tin sản phẩm
     */
    public Page<ProductDetailResponse> searchProducts(
            String keyword, Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage;

        // Tìm kiếm theo từ khóa và danh mục
        if (keyword != null && categoryId != null) {
            productPage =
                    productRepository.findByNameContainingIgnoreCaseAndCategoryId(
                            keyword, categoryId, pageable);
        } else if (keyword != null) {
            // Tìm kiếm chỉ theo từ khóa
            productPage = productRepository.findByNameContainingIgnoreCase(keyword, pageable);
        } else if (categoryId != null) {
            // Tìm kiếm chỉ theo danh mục
            productPage = productRepository.findByCategoryId(categoryId, pageable);
        } else {
            // Lấy tất cả sản phẩm
            productPage = productRepository.findAll(pageable);
        }

        return productPage.map(this::convertToProductDetailResponse);
    }

    /**
     * Chuyển đổi từ Product entity sang ProductDetailResponse
     * 
     * @param product Entity Product cần chuyển đổi
     * @return ProductDetailResponse Chứa thông tin chi tiết sản phẩm
     */
    private ProductDetailResponse convertToProductDetailResponse(Product product) {
        return new ProductDetailResponse(
                product.getId(), product.getName(), product.getPrice(), product.getCategory().getName());
    }

    /**
     * Lấy danh sách sản phẩm theo danh mục
     * 
     * @param categoryId ID của danh mục
     * @param page Số trang (bắt đầu từ 0)
     * @param size Số lượng phần tử trên mỗi trang
     * @return Page<ProductDetailResponse> Trang dữ liệu chứa thông tin sản phẩm
     */
    public Page<ProductDetailResponse> getProductsByCategory(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findByCategoryId(categoryId, pageable);
        return productPage.map(this::convertToProductDetailResponse);
    }

    /**
     * Cập nhật trạng thái sản phẩm
     * 
     * @param request Thông tin cập nhật trạng thái
     * @throws RuntimeException Nếu sản phẩm hoặc người dùng không tồn tại
     */
    @Override
    public void updateProductStatus(UpdateProductStatusRequest request) {
        // Kiểm tra và lấy thông tin sản phẩm
        Product product =
                productRepository
                        .findById(request.getProductId())
                        .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        // Kiểm tra và lấy thông tin người dùng
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

    /**
     * Lấy danh sách tất cả sản phẩm có phân trang
     * 
     * @param page Số trang (bắt đầu từ 0)
     * @param size Số lượng phần tử trên mỗi trang
     * @return Page<ProductDetailResponse> Trang dữ liệu chứa thông tin sản phẩm
     */
    @Override
    public Page<ProductDetailResponse> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(this::convertToProductDetailResponse);
    }
}