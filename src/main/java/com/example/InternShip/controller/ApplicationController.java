package com.example.InternShip.controller;

import com.example.InternShip.dto.request.ApplicationRequest;
import com.example.InternShip.dto.response.ApplicationResponse;
import com.example.InternShip.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping(consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApplicationResponse> submit(
            @ModelAttribute @Valid ApplicationRequest request) {
        return ResponseEntity.ok(applicationService.submitApplication(request));
    }


    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApplicationResponse> getMyApplication() {
        ApplicationResponse resp = applicationService.getMyApplication();
        if (resp == null) {
            return ResponseEntity.ok().build(); // 200 với body rỗng (frontend nhận null/undefined)
        }
        return ResponseEntity.ok(resp);
    }

}
