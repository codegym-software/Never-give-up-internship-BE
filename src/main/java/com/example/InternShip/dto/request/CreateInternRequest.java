package com.example.InternShip.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateInternRequest {

    @NotBlank(message = "FULL_NAME_INVALID")
    private String fullName;

    @NotBlank(message = "EMAIL_INVALID")
    @Email(message = "EMAIL_INVALID")
    private String email;

    @Pattern(regexp = "^$|0\\d{9}", message = "PHONE_INVALID")
    private String phone;

    @Size(min = 5, max = 200, message = "Địa chỉ quá dài hoặc quá ngắn")
    private String address;

    @NotNull(message = "MAJOR_NOTNULL")
    private Integer majorId;

    @NotNull(message = "UNIVERSITY_NOTNULL")
    private Integer universityId;
}
