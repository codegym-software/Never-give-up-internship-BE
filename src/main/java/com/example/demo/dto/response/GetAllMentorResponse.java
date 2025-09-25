package com.example.demo.dto.response;

import lombok.Data;

@Data
public class GetAllMentorResponse {
    private Integer id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String avatar;
    private boolean isActive;
    private String specialization;
    private int experienceYears;
    private long groupCount;
}
