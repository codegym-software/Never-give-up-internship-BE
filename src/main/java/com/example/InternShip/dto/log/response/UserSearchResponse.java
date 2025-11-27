package com.example.InternShip.dto.log.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSearchResponse {
    private Integer id;
    private String fullName;
    private String email;
}