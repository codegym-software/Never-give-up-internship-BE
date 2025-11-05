package com.example.InternShip.service;

import com.example.InternShip.dto.request.CreateSprintRequest;
import com.example.InternShip.dto.request.UpdateSprintRequest;
import com.example.InternShip.dto.response.PagedResponse;
import com.example.InternShip.dto.response.SprintResponse;

import java.util.List;

public interface SprintService {
    SprintResponse createSprint(Integer teamId, CreateSprintRequest request);
    SprintResponse updateSprint(Long sprintId, UpdateSprintRequest request);
    void deleteSprint(Long sprintId);
    PagedResponse<SprintResponse> getSprintsByTeam(Integer teamId, int page, int size);
    SprintResponse getSprintById(Long sprintId);
}
