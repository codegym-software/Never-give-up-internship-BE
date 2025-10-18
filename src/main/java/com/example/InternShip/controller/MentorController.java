package com.example.InternShip.controller;

import com.example.InternShip.service.MentorService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mentors")
@RequiredArgsConstructor
public class MentorController {
    private final MentorService mentorService;

    @GetMapping // Hàm lấy ra danh sách mentor
    public ResponseEntity<?> getAll(
            @RequestParam(required = false, defaultValue = "") List<Integer> department,
            @RequestParam(required = false, defaultValue = "") String keyword, // fullName, Email, Phone, Address
            @RequestParam(required = false, defaultValue = "1") int page) {

        return ResponseEntity.ok(mentorService.getAll(
                department,
                keyword,
                page));
    }
}
