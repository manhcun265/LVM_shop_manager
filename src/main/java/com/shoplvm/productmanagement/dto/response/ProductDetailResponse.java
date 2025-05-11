package com.shoplvm.productmanagement.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDetailResponse {
    Long productId;
    String name;
    BigDecimal price;
    String categoryName;

    public ProductDetailResponse(Long id, String name, BigDecimal price, String categoryName) {
        this.productId = id;
        this.name = name;
        this.price = price;
        this.categoryName = categoryName;
    }
}

