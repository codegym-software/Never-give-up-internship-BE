package com.example.InternShip.service.impl;

import com.example.InternShip.dto.request.CreateSprintRequest;
import com.example.InternShip.dto.request.UpdateSprintRequest;
import com.example.InternShip.dto.response.PagedResponse;
import com.example.InternShip.dto.response.SprintResponse;
import com.example.InternShip.entity.Team;
import com.example.InternShip.entity.Sprint;
import com.example.InternShip.exception.ProgramNotFoundException;
import com.example.InternShip.exception.SprintConflictException;
import com.example.InternShip.exception.SprintUpdateException;
import com.example.InternShip.exception.InvalidSprintDateException;
import com.example.InternShip.repository.TeamRepository;
import com.example.InternShip.repository.SprintRepository;
import com.example.InternShip.service.SprintService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SprintServiceImpl implements SprintService {

    private final SprintRepository sprintRepository;
    private final TeamRepository teamRepository;
    private final ModelMapper modelMapper;

    @Override
    public SprintResponse createSprint(Integer teamId, CreateSprintRequest request) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ProgramNotFoundException("Team not found"));

        // Validate new sprint dates
        LocalDate today = LocalDate.now();
        if (request.getStartDate().isBefore(today)) {
            throw new InvalidSprintDateException("Sprint start date cannot be in the past.");
        }
        if (request.getEndDate().isBefore(today)) {
            throw new InvalidSprintDateException("Sprint end date cannot be in the past.");
        }
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new InvalidSprintDateException("Sprint start date cannot be after end date.");
        }

        // Check for overlapping sprints
        List<Sprint> existingSprints = team.getSprints();
        for (Sprint existingSprint : existingSprints) {
            // Check for overlap: (StartA <= EndB) and (EndA >= StartB)
            if (request.getStartDate().isBefore(existingSprint.getEndDate()) &&
                request.getEndDate().isAfter(existingSprint.getStartDate())) {
                throw new SprintConflictException("Sprint dates overlap with an existing sprint.");
            }
        }

        Sprint sprint = new Sprint();
        sprint.setName(request.getName());
        sprint.setGoal(request.getGoal());
        sprint.setStartDate(request.getStartDate());
        sprint.setEndDate(request.getEndDate());
        sprint.setTeam(team);

        Sprint savedSprint = sprintRepository.save(sprint);
        return modelMapper.map(savedSprint, SprintResponse.class);
    }

    @Override
    public SprintResponse updateSprint(Long sprintId, UpdateSprintRequest request) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new RuntimeException("Sprint not found"));

        if (sprint.getEndDate().isBefore(LocalDate.now())) {
            throw new SprintUpdateException("Không thể cập nhật sprint đã hoàn thành.");
        }

        sprint.setName(request.getName());
        sprint.setGoal(request.getGoal());
        sprint.setStartDate(request.getStartDate());
        sprint.setEndDate(request.getEndDate());

        Sprint updatedSprint = sprintRepository.save(sprint);
        return modelMapper.map(updatedSprint, SprintResponse.class);
    }

    @Override
    public void deleteSprint(Long sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new RuntimeException("Sprint not found"));

        if (!sprint.getStartDate().isAfter(LocalDate.now())) {
            throw new SprintUpdateException("Chỉ có thể xóa sprint chưa bắt đầu.");
        }

        sprintRepository.deleteById(sprintId);
    }

    @Override
    public PagedResponse<SprintResponse> getSprintsByTeam(Integer teamId, int page, int size) {
        teamRepository.findById(teamId)
                .orElseThrow(() -> new ProgramNotFoundException("Team not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("startDate").descending());

        Page<Sprint> sprintPage = sprintRepository.findByTeamId(teamId, pageable);

        List<SprintResponse> sprintResponses = sprintPage.getContent().stream()
                .map(sprint -> modelMapper.map(sprint, SprintResponse.class))
                .collect(Collectors.toList());

        return new PagedResponse<>(
                sprintResponses,
                sprintPage.getNumber(),
                sprintPage.getTotalElements(),
                sprintPage.getTotalPages(),
                sprintPage.hasNext(),
                sprintPage.hasPrevious()
        );
    }

    @Override
    public SprintResponse getSprintById(Long sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new RuntimeException("Sprint not found"));
        return modelMapper.map(sprint, SprintResponse.class);
    }
}
