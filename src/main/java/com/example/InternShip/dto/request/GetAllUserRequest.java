package com.example.InternShip.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GetAllUserRequest {
    private String keyword;
    private String role;
    private int page;
}
