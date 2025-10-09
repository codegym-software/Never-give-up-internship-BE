package com.example.InternShip.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ApplicationRequest {
    @NotNull(message = "INTERNSHIP_TERM_INVALID")
    private Integer internshipTermId;

    @NotNull(message = "UNIVERSITY_INVALID")
    private Integer universityId;

    @NotNull(message = "MAJOR_INVALID")
    private Integer majorId;

    @NotNull(message = "")
    private MultipartFile cvFile;
}
