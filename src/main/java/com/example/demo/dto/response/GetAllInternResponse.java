package com.example.demo.dto.response;

import lombok.Data;

@Data
public class GetAllInternResponse {
    private Integer id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String avatar;
    private boolean isActive;
}
