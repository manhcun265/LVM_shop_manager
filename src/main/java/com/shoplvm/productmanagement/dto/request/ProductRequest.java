package com.shoplvm.productmanagement.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO chứa thông tin yêu cầu tạo/cập nhật sản phẩm
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    
    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String name;

    @NotNull(message = "Giá sản phẩm không được để trống")
    @Min(value = 0, message = "Giá sản phẩm phải lớn hơn hoặc bằng 0")
    private BigDecimal price;

    @NotNull(message = "Danh mục sản phẩm không được để trống")
    private Long categoryId;

    private String detail;

    @NotNull(message = "Danh sách hình ảnh không được để trống")
    private List<String> imageUrls;

    @NotNull(message = "ID người dùng không được để trống")
    private Long userId;
}
