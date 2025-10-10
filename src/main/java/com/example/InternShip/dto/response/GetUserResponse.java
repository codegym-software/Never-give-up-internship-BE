package com.example.InternShip.dto.response;

import com.example.InternShip.entity.enums.Role;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

@Data
public class GetUserResponse {
    private int id;
    private String fullName;
    private String username;
    private String email;
    private String phone;
    private String address;
    private boolean isActive;
    private Role role;
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedAt;
}
