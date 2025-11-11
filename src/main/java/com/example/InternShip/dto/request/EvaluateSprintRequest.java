package com.example.InternShip.dto.request;

import lombok.Data;

@Data
public class EvaluateSprintRequest {
    private String feedbackGood;
    private String feedbackBad;
    private String feedbackImprove;
}
