package com.example.InternShip.dto.request;

import com.example.InternShip.entity.InternshipApplication;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateApplicationStatusRequest {
    @NotNull(message = "STATUS_INVALID")
    private InternshipApplication.Status status;
}