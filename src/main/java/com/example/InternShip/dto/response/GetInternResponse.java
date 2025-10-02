package com.example.InternShip.dto.response;

import com.example.InternShip.entity.Intern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetInternResponse {
    private int id;
    private String fullName;
    private String email;
    private String phone;
    private String major;
    private String university;
    private Intern.Status status;
}
