package com.example.InternShip.controller;

import com.example.InternShip.dto.request.CreateMentorRequest;
import com.example.InternShip.dto.request.UpdateMentorRequest;
import com.example.InternShip.dto.response.MentorResponse;
import com.example.InternShip.service.MentorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mentors")
@RequiredArgsConstructor
public class MentorController {
    private final MentorService mentorService;

    @PostMapping
    public ResponseEntity<MentorResponse> createMentor(@Valid @RequestBody CreateMentorRequest request) {
        return ResponseEntity.ok(mentorService.createMentor(request));
    }

    @PutMapping("/{id}/department")
    public ResponseEntity<MentorResponse> updateMentorDepartment(
            @PathVariable("id") Integer id,
            @RequestBody @Valid UpdateMentorRequest request) {
        return ResponseEntity.ok(mentorService.updateMentorDepartment(id, request));
    }
}