package com.example.InternShip.service;

import java.util.List;

import com.example.InternShip.dto.response.GetAllTeamResponse;
import com.example.InternShip.dto.response.PagedResponse;

public interface TeamService {

    PagedResponse<GetAllTeamResponse> getAllTeam(List<Integer> internshipTerm, List<Integer> mentor, String keyword, int page);
}
