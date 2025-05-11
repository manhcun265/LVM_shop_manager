package com.shoplvm.productmanagement.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
    @NotBlank(message = "Tên sản phẩm không được để trống")
    String name;

    @NotNull(message = "Giá không được để trống")
    @Min(value = 0, message = "Giá phải >= 0")
    BigDecimal price;

    @NotNull(message = "Danh mục không được để trống")
    Long categoryId;

    Map<String, Object> detail;

    List<@NotBlank(message = "URL ảnh không được để trống") String> imageUrls;

    @NotNull(message = "Người tạo không được để trống")
    Long userId;
}
