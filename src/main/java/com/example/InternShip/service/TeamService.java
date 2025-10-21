package com.example.InternShip.service;

import com.example.InternShip.dto.request.AddMemberRequest;
import com.example.InternShip.dto.request.CreateTeamRequest;
import com.example.InternShip.dto.response.TeamDetailResponse;

public interface TeamService {
    TeamDetailResponse createTeam(CreateTeamRequest request);
    TeamDetailResponse getTeamDetails(Integer teamId);
    TeamDetailResponse addMember(Integer teamId, AddMemberRequest request);
    void removeMember(Integer teamId, Integer internId);
}
