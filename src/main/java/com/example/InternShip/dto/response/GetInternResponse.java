package com.example.InternShip.dto.response;

import com.example.InternShip.entity.Intern;
import lombok.Data;

@Data
public class GetInternResponse {
    private int id;
    private String fullName;
    private String email;
    private String phone;
    private String major;
    private String university;
    private Intern.Status status;
}
