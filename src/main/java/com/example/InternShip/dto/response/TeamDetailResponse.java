package com.example.InternShip.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class TeamDetailResponse {
    private Integer id;
    private String teamName;
    private String internshipProgramName;
    private String mentorName;
    private List<GetInternResponse> members;
}