package com.example.InternShip.service.impl;

import com.example.InternShip.dto.request.CreateSprintRequest;
import com.example.InternShip.dto.request.UpdateSprintRequest;
import com.example.InternShip.dto.response.SprintResponse;
import com.example.InternShip.entity.Team;
import com.example.InternShip.entity.Sprint;
import com.example.InternShip.exception.ErrorCode;
import com.example.InternShip.repository.TeamRepository;
import com.example.InternShip.repository.SprintRepository;
import com.example.InternShip.service.SprintService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.example.InternShip.entity.User;
import com.example.InternShip.entity.enums.Role;
import com.example.InternShip.service.AuthService;

import com.example.InternShip.repository.InternRepository;
import com.example.InternShip.entity.Intern;

@Service
@RequiredArgsConstructor
public class SprintServiceImpl implements SprintService {

    private final SprintRepository sprintRepository;
    private final TeamRepository teamRepository;
    private final AuthService authService;
    private final InternRepository internRepository;

    @Override
    public SprintResponse createSprint(Integer teamId, CreateSprintRequest request) {
        // Validate new sprint dates
        LocalDate today = LocalDate.now();
        if (request.getStartDate().isBefore(today)) {
            throw new IllegalArgumentException(ErrorCode.TIME_START_INVALID.getMessage());
        }
        if (request.getEndDate().isBefore(today)) {
            throw new IllegalArgumentException(ErrorCode.TIME_END_INVALID.getMessage());
        }
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new IllegalArgumentException(ErrorCode.TIME_INVALID.getMessage());
        }

        User user = authService.getUserLogin();
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.TEAM_NOT_EXISTED.getMessage()));

        checkSprintManagementPermission(user, team, "create");

        // Check for overlapping sprints
        List<Sprint> existingSprints = team.getSprints();
        for (Sprint existingSprint : existingSprints) {
            // Check for overlap: (StartA <= EndB) and (EndA >= StartB)
            if (request.getStartDate().isBefore(existingSprint.getEndDate()) &&
                    request.getEndDate().isAfter(existingSprint.getStartDate())) {
                throw new IllegalArgumentException(ErrorCode.TIME_INVALID.getMessage());
            }
        }

        Sprint sprint = new Sprint();
        sprint.setName(request.getName());
        sprint.setGoal(request.getGoal());
        sprint.setStartDate(request.getStartDate());
        sprint.setEndDate(request.getEndDate());
        sprint.setTeam(team);

        Sprint savedSprint = sprintRepository.save(sprint);
        return mapToSprintResponse(savedSprint);
    }

    public SprintResponse mapToSprintResponse(Sprint sprint) {

        SprintResponse response = new SprintResponse();
        response.setId(sprint.getId());
        response.setName(sprint.getName());
        response.setGoal(sprint.getGoal());
        response.setStartDate(sprint.getStartDate());
        response.setEndDate(sprint.getEndDate());
        response.setTeamId(sprint.getTeam().getId());
        return response;
    }

    private void checkSprintManagementPermission(User user, Team team, String action) {
        // HR can view any sprint
        if (action.equals("view") && user.getRole().equals(Role.HR)) {
            return;
        }

        // Interns can view sprints of their own team
        if (action.equals("view") && user.getRole().equals(Role.INTERN)) {
            Intern intern = internRepository.findByUser(user)
                    .orElseThrow(() -> new EntityNotFoundException("Intern profile not found for the current user."));
            if (intern.getTeam() != null && intern.getTeam().getId().equals(team.getId())) {
                return; // Allowed
            }
        }

        // Mentor can manage sprints of their own team
        if (user.getRole().equals(Role.MENTOR)) {
            if (team.getMentor() != null && team.getMentor().getUser().getId().equals(user.getId())) {
                return; // Allowed
            }
        }
        
        // If no permission rule matched, deny access.
        throw new AccessDeniedException(ErrorCode.NOT_PERMISSION.getMessage());
    }

    @Override
    public SprintResponse updateSprint(Long sprintId, UpdateSprintRequest request) {
        User user = authService.getUserLogin();
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.SPRINT_NOT_EXISTS.getMessage()));

        Team team = sprint.getTeam();
        checkSprintManagementPermission(user, team, "update");

        // Apply partial updates for non-date fields first
        if (request.getName() != null) {
            sprint.setName(request.getName());
        }
        if (request.getGoal() != null) {
            sprint.setGoal(request.getGoal());
        }

        // Handle date updates based on sprint status
        LocalDate today = LocalDate.now();
        boolean isSprintStarted = !today.isBefore(sprint.getStartDate());
        boolean isSprintEnded = today.isAfter(sprint.getEndDate());

        if (isSprintEnded) {
            throw new RuntimeException(ErrorCode.UPDATE_SPRINT_FAILED.getMessage());
        }

        if (isSprintStarted) { // Sprint is in progress
            if (request.getStartDate() != null && !request.getStartDate().equals(sprint.getStartDate())) {
                throw new RuntimeException(ErrorCode.UPDATE_SPRINT_FAILED.getMessage());
            }
            if (request.getEndDate() != null) {
                if (request.getEndDate().isBefore(sprint.getEndDate())) {
                    throw new RuntimeException(ErrorCode.UPDATE_SPRINT_FAILED.getMessage());
                }
                sprint.setEndDate(request.getEndDate());
            }
        } else { // Sprint has not started yet
            LocalDate newStartDate = request.getStartDate() != null ? request.getStartDate() : sprint.getStartDate();
            LocalDate newEndDate = request.getEndDate() != null ? request.getEndDate() : sprint.getEndDate();

            if (request.getStartDate() != null || request.getEndDate() != null) {
                if (newStartDate.isBefore(today)) {
                    throw new IllegalArgumentException(ErrorCode.TIME_START_INVALID.getMessage());
                }
                if (newEndDate.isBefore(today)) {
                    throw new IllegalArgumentException(ErrorCode.TIME_END_INVALID.getMessage());
                }
                if (newStartDate.isAfter(newEndDate)) {
                    throw new IllegalArgumentException(ErrorCode.TIME_INVALID.getMessage());
                }

                // Check for overlapping sprints
                List<Sprint> otherSprints = team.getSprints().stream()
                        .filter(s -> !s.getId().equals(sprintId))
                        .toList();
                for (Sprint existingSprint : otherSprints) {
                    if (newStartDate.isBefore(existingSprint.getEndDate())
                            && newEndDate.isAfter(existingSprint.getStartDate())) {
                        throw new IllegalArgumentException(ErrorCode.TIME_INVALID.getMessage());
                    }
                }
                sprint.setStartDate(newStartDate);
                sprint.setEndDate(newEndDate);
            }
        }

        Sprint updatedSprint = sprintRepository.save(sprint);
        return mapToSprintResponse(updatedSprint);
    }


    @Override
    public void deleteSprint(Long sprintId) {
        User user = authService.getUserLogin();
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.SPRINT_NOT_EXISTS.getMessage()));
        Team team = sprint.getTeam();

        checkSprintManagementPermission(user, team, "delete");

        if (!sprint.getStartDate().isAfter(LocalDate.now())) {
            throw new RuntimeException(ErrorCode.DELETE_SPRINT_FAILED.getMessage());
        }

        sprintRepository.deleteById(sprintId);
    }

    @Override
    public List<SprintResponse> getSprintsByTeam(Integer teamId) {
        User user = authService.getUserLogin();
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.TEAM_NOT_EXISTED.getMessage()));

        checkSprintManagementPermission(user, team, "view");

        List<Sprint> sprints = sprintRepository.findByTeamId(teamId);

        return sprints.stream()
                .map(this::mapToSprintResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SprintResponse getSprintById(Long sprintId) {
        User user = authService.getUserLogin();
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.SPRINT_NOT_EXISTS.getMessage()));
        Team team = sprint.getTeam();

        checkSprintManagementPermission(user, team, "view");

        return mapToSprintResponse(sprint);
    }
}
