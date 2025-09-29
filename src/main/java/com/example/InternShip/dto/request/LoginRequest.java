package com.example.InternShip.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "USERNAME_INVALID")
    private String identifier;
    @NotBlank(message = "PASSWORD_INVALID")
    private String password;
}
