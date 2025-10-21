package com.example.InternShip.controller;

import com.example.InternShip.dto.response.GetAllInternProgramResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.InternShip.service.InternshipProgramService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/internship-programs")
@RequiredArgsConstructor
public class InternshipProgramController {

    private final InternshipProgramService internshipProgramService;
    @GetMapping // Cái này chắc là cho bên client
    public List<GetAllInternProgramResponse> getAllPrograms() {
        return internshipProgramService.getAllPrograms();
    }

    @GetMapping("/get") // Hàm lấy ra các chương trình thực tập (Cái này cho bên Manager)
    public ResponseEntity<?> getAllInternshipPrograms(
            @RequestParam(required = false, defaultValue = "") List<Integer> department,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "1") int page) {

        return ResponseEntity.ok(internshipProgramService.getAllInternshipPrograms(
                department,
                keyword,
                page));
    }
}
