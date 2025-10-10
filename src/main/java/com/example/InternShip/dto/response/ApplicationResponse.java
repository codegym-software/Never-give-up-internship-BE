package com.example.InternShip.dto.response;

import com.example.InternShip.entity.InternshipApplication;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ApplicationResponse {
    private Integer applicationId;
    private String internshipTerm;
    private String university;
    private String major;
    private String applicantName;
    private InternshipApplication.Status status;
    private LocalDateTime appliedDate;
    private String message;
}

