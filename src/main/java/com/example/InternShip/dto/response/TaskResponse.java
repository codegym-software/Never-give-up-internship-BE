package com.example.InternShip.dto.response;

import com.example.InternShip.entity.enums.TaskStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskResponse {
    private Long id;
    private String name;
    private String description;
    private TaskStatus status;
    private LocalDate deadline;
    private Long sprintId;
    private Integer internId;
    private Integer mentorId;
}
