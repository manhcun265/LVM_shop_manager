package com.shoplvm.productmanagement.dto.request;

import com.shoplvm.productmanagement.entity.ProductStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProductStatusRequest {
    Long productId;
    ProductStatus status;
    Long userId;

}
