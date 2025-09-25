package com.example.demo.dto.request;

import lombok.Data;

@Data
public class GetAllUserRequest {
    private String keyword;
    private int page = 1;
    private int size = 10;
}
