package com.example.InternShip.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


import org.springframework.web.multipart.MultipartFile;


@Data

public class UpdateInfoRequest {
    @NotBlank(message = "FULL_NAME_INVALID")
    private String fullName;
// áp dụng biểu thức chính quy để kiểm tra định dạng số điện thoại: bắt đầu bằng dấu + tùy chọn, theo sau là 7 đến 15 chữ số
    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "PHONE_INVALID")
    private String phone;
    private String address;
    private MultipartFile avatarFile;
}
