package com.example.demo.dto.request;

import com.example.demo.exception.ErrorCode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateDemoRequest {
    @NotBlank(message = "NAME_NOT_EMPTY")
    private String hoTen;
    @NotBlank(message = "EMAIL_NOT_EMPTY")
    @Email(message = "INVALID_EMAIL")
    private String email;
    private LocalDate ngaySinh;
    @NotBlank(message = "GENDER_NOT_EMPTY")
    @Pattern(regexp = "^(Nam|Nữ)$", message = "INVALID_GENDER")
    private String gioiTinh;
}
