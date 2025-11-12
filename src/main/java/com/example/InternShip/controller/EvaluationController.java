package com.example.InternShip.controller;

import com.example.InternShip.dto.request.EvaluateInternRequest;
import com.example.InternShip.dto.response.EvaluationResponse;
import com.example.InternShip.service.EvaluationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/evaluations")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    @PutMapping("/interns/{id}")
    //@PreAuthorize("hasAuthority('SCOPE_MENTOR')")
    public ResponseEntity<EvaluationResponse> evaluateIntern(
            @PathVariable("id") Integer internId,
            @RequestBody @Valid EvaluateInternRequest request) {

        EvaluationResponse response = evaluationService.evaluateIntern(internId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/interns/{id}")
    //@PreAuthorize("hasAuthority('SCOPE_MENTOR') or hasAuthority('SCOPE_HR') or @authService.isSelf(authentication, #internId)")
    public ResponseEntity<EvaluationResponse> getEvaluation(
            @PathVariable("id") Integer internId) {

        EvaluationResponse response = evaluationService.getEvaluation(internId);
        return ResponseEntity.ok(response);
    }
}