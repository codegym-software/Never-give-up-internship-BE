package com.example.InternShip.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateTaskRequest {
    private String name;
    private String description;
    private Long sprintId;
    private Integer internId;
    private LocalDate deadline;
}
