package com.example.InternShip.service;

import com.example.InternShip.dto.request.EvaluateInternRequest;
import com.example.InternShip.dto.response.EvaluationResponse;

public interface EvaluationService {
    EvaluationResponse evaluateIntern(Integer internId, EvaluateInternRequest request);
    EvaluationResponse getEvaluation(Integer internId);
}