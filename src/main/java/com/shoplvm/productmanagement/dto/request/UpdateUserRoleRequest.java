package com.shoplvm.productmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateUserRoleRequest {
    @NotBlank(message = "Vai trò không được để trống")
    @Pattern(regexp = "^(ADMIN|USER)$", message = "Vai trò phải là ADMIN hoặc USER")
    private String role;
} 