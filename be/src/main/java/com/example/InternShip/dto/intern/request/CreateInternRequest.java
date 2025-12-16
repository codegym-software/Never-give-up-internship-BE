package com.example.InternShip.dto.intern.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateInternRequest {

    @NotBlank(message = "FULL_NAME_NOT_NULL")
    private String fullName;

    @NotBlank(message = "EMAIL_NOT_NULL")
    @Email(message = "EMAIL_INVALID")
    private String email;

    @Pattern(regexp = "^$|0\\d{9}", message = "PHONE_INVALID")
    private String phone;

    private String address;

    @NotNull(message = "MAJOR_NOT_NULL")
    private Integer majorId;

    @NotNull(message = "UNIVERSITY_NOT_NULL")
    private Integer universityId;

    @NotNull(message = "INTERNSHIP_PROGRAM_NOT_NULL")
    private Integer internshipProgramId;
}
