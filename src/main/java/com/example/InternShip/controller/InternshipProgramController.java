package com.example.InternShip.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.InternShip.entity.InternshipProgram;
import com.example.InternShip.service.InternshipProgramService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/internship-programs")
@RequiredArgsConstructor
public class InternshipProgramController {

    private final InternshipProgramService internshipProgramService;
    @GetMapping
    public List<InternshipProgram> getAllPrograms() {
        return internshipProgramService.getAllPrograms();
    }
}
