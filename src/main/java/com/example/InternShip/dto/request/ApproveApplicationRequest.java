package com.example.InternShip.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ApproveApplicationRequest {
    @NotEmpty(message = "LIST_APPLICATION_INVALID")
    Set<Integer> applicationIds;
}
