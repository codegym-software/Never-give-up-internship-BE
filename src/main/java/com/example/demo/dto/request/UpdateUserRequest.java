package com.example.demo.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateUserRequest {
    private String fullName;
    @Pattern(regexp = "^$|0\\d{9}", message = "INVALID_PHONE")
    private String phone;
    private String address;
}
