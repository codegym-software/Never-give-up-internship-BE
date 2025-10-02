package com.example.InternShip.dto.request;


import java.io.ObjectInputFilter.Status;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateInternRequest {

    private Integer majorId;

    private Integer universityId;

    private String status; 
}
