package com.example.InternShip.service;

import com.example.InternShip.dto.request.AddMemberRequest;
import com.example.InternShip.dto.request.CreateTeamRequest;
import com.example.InternShip.dto.response.GetAllTeamResponse;
import com.example.InternShip.dto.response.PagedResponse;
import com.example.InternShip.dto.response.TeamDetailResponse;

import java.util.List;

public interface TeamService {
    TeamDetailResponse createTeam(CreateTeamRequest request);
    TeamDetailResponse getTeamDetails(Integer teamId);
    TeamDetailResponse addMember(Integer teamId, AddMemberRequest request);
    void removeMember(Integer teamId, Integer internId);
    PagedResponse<GetAllTeamResponse> getAllTeam(List<Integer> internshipTerm, List<Integer> mentor, String keyword, int page);
}
