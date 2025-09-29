package com.example.InternShip.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateInfoRequest {
    @NotBlank(message = "FULL_NAME_INVALID")
    private String fullName;
    private String phone;
    private String address;
}
