package com.example.InternShip.dto.response;

import com.example.InternShip.entity.InternshipApplication;
import com.example.InternShip.entity.InternshipProgram;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponse {
     private Integer id;
    private String cvUrl;
    private String internshipApplicationtUrl;
    private String internshipContractUrl;
    private String internshipApplicationStatus;
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDateTime createdAt;

    private String internshipProgram;
    private String universityName;
    private String majorName;

    private String fullName;
    private String email;
    private String phone;
    private String address;
}

