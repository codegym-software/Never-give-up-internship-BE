package com.example.InternShip.controller;

import com.example.InternShip.service.TeamService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @GetMapping // Hàm lấy ra danh sách đơn xin thực tập
    public ResponseEntity<?> getAllTeam(
            @RequestParam(required = false, defaultValue = "") List<Integer> internshipProgram,
            @RequestParam(required = false, defaultValue = "") List<Integer> mentor,
            @RequestParam(required = false, defaultValue = "") String keyword, // Tên nhóm
            @RequestParam(required = false, defaultValue = "1") int page) {

        return ResponseEntity.ok(teamService.getAllTeam(
                internshipProgram,
                mentor,
                keyword,
                page));
    }
}
