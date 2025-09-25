package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank(message = "FULL_NAME_NOT_EMPTY")
    private String fullName;
    @Pattern(regexp = "^$|0\\d{9}", message = "INVALID_PHONE")
    private String phone;
    private String address;
    private String role;
}
