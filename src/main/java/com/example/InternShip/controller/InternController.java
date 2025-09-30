package com.example.InternShip.controller;

import com.example.InternShip.dto.request.CreateInternRequest;
import com.example.InternShip.dto.response.InternResponse;
import com.example.InternShip.entity.Intern;
import com.example.InternShip.repository.InternRepository;
import com.example.InternShip.service.InternService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/interns")
@RequiredArgsConstructor
public class InternController {
    private final InternService internService;

    @PostMapping
    public ResponseEntity<InternResponse> createIntern(@Valid @RequestBody CreateInternRequest request){
        return ResponseEntity.ok(internService.createIntern(request));
    }
}
