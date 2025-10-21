package com.example.InternShip.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTeamRequest {

    @NotBlank(message = "NAME_INVALID")
    private String name;

    @NotNull(message = "PROGRAM_ID_INVALID")
    private Integer internshipProgramId;

    @NotNull(message = "MENTOR_ID_INVALID")
    private Integer mentorId;
}