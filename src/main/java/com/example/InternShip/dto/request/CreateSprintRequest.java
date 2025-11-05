package com.example.InternShip.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateSprintRequest {
    private String name;
    private String goal;
    private LocalDate startDate;
    private LocalDate endDate;

}
