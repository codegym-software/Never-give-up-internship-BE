package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "INVALID_LOGIN")
    private String identifier;
    @NotBlank(message = "INVALID_LOGIN")
    private String password;
}
