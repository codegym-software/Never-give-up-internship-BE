package com.example.InternShip.controller;

import com.example.InternShip.dto.team.request.AddMemberRequest;
import com.example.InternShip.dto.team.request.CreateTeamRequest;
import com.example.InternShip.dto.team.request.UpdateTeamRequest;
import com.example.InternShip.dto.team.response.TeamDetailResponse;
import com.example.InternShip.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @PreAuthorize("hasAuthority('SCOPE_HR') or hasAuthority('SCOPE_ADMIN')")
    @PostMapping
    public ResponseEntity<TeamDetailResponse> createTeam(@RequestBody @Valid CreateTeamRequest request) {
        return ResponseEntity.ok(teamService.createTeam(request));
    }

    @PreAuthorize("hasAuthority('SCOPE_HR') or hasAuthority('SCOPE_ADMIN')")
    @PostMapping("/{teamId}/members")
    public ResponseEntity<TeamDetailResponse> addMember(
            @PathVariable Integer teamId,
            @RequestBody @Valid AddMemberRequest request) {
        return ResponseEntity.ok(teamService.addMember(teamId, request));
    }

    @PreAuthorize("hasAuthority('SCOPE_HR') or hasAuthority('SCOPE_ADMIN')")
    @GetMapping // Hàm lấy ra danh sách đơn xin thực tập
    public ResponseEntity<?> getAllTeam(
            @RequestParam(required = false, defaultValue = "") Integer internshipProgram,
            @RequestParam(required = false, defaultValue = "") Integer mentor,
            @RequestParam(required = false, defaultValue = "") String keyword, // Tên nhóm
            @RequestParam(required = false, defaultValue = "1") int page) {

        return ResponseEntity.ok(teamService.getTeams(
                internshipProgram,
                mentor,
                keyword,
                page));
    }

    @GetMapping("/my-teams")
    @PreAuthorize("hasAuthority('SCOPE_MENTOR')")
    public ResponseEntity<List<TeamDetailResponse>> getMyTeams() {
        return ResponseEntity.ok(teamService.getTeamsByCurrentMentor());
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllTeam() {
        return ResponseEntity.ok(teamService.getAllTeam());
    }

    @PatchMapping("/remove/{internId}")
    @PreAuthorize("hasAuthority('SCOPE_HR') or hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<?> removeMember(@PathVariable Integer internId) {
        return ResponseEntity.ok(teamService.removeMember(internId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamDetailResponse> getTeamById(@PathVariable Integer id) {
        return ResponseEntity.ok(teamService.getTeamById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_HR') or hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<TeamDetailResponse> updateTeam(
            @PathVariable Integer id,
            @RequestBody @Valid UpdateTeamRequest request) {
        return ResponseEntity.ok(teamService.updateTeam(id, request));
    }

    @GetMapping("/by/{internshipProgramId}")
    public ResponseEntity<?> getAllTeamByIP(@PathVariable Integer internshipProgramId) {
        return ResponseEntity.ok(teamService.getAllTeamByIP(internshipProgramId));
    }
}