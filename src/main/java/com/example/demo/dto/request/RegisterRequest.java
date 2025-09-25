package com.example.demo.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "USERNAME_NOT_EMPTY")
    @Size(min = 5,max = 50, message = "INVALID_USERNAME")
    private String username;

    @Email(message = "INVALID_EMAIL")
    @NotBlank(message = "EMAIL_NOT_EMPTY")
    private String email;

    @NotBlank(message = "PASSWORD_NOT_EMPTY")
    @Size(min = 8,max = 20, message = "INVALID_PASSWORD")
    private String password;

    @NotBlank(message = "NAME_NOT_EMPTY")
    private String fullName;

}
