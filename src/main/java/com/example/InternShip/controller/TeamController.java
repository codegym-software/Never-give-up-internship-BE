package com.example.InternShip.controller;

import com.example.InternShip.dto.request.AddMemberRequest;
import com.example.InternShip.dto.request.CreateTeamRequest;
import com.example.InternShip.dto.response.TeamDetailResponse;
import com.example.InternShip.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<TeamDetailResponse> createTeam(@RequestBody @Valid CreateTeamRequest request) {
        return ResponseEntity.ok(teamService.createTeam(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamDetailResponse> getTeamDetails(@PathVariable Integer id) {
        return ResponseEntity.ok(teamService.getTeamDetails(id));
    }

    @PostMapping("/{teamId}/members")
    public ResponseEntity<TeamDetailResponse> addMember(
            @PathVariable Integer teamId,
            @RequestBody @Valid AddMemberRequest request) {
        return ResponseEntity.ok(teamService.addMember(teamId, request));
    }

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

    @PatchMapping("/{internId}")
    public ResponseEntity<Void> removeMember(@PathVariable Integer internId) {
        teamService.removeMember(internId);
        return ResponseEntity.ok().build();
    }
}