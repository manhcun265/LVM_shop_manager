package com.shoplvm.productmanagement.dto.response;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDetailResponse {
  Long productId;
  String name;
  BigDecimal price;
  String categoryName;
}
