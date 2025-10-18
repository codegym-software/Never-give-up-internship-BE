package com.example.InternShip.dto.response;

import java.time.LocalDate;

import com.example.InternShip.entity.InternshipProgram;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetAllInternProgramManagerResponse {
    private Integer id;
    private String name;

    private LocalDate endPublishedTime;
    private LocalDate endReviewingTime;
    private LocalDate timeStart;
    private LocalDate timeEnd;

    private InternshipProgram.Status status;
}
