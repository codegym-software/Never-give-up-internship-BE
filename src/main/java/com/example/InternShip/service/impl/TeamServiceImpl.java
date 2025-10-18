package com.example.InternShip.service.impl;

import com.example.InternShip.dto.response.GetAllTeamResponse;
import com.example.InternShip.dto.response.PagedResponse;
import com.example.InternShip.entity.Team;
import com.example.InternShip.repository.TeamRepository;
import com.example.InternShip.service.TeamService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;

    private final ModelMapper modelMapper;

    @Override
    public PagedResponse<GetAllTeamResponse> getAllTeam(List<Integer> internshipProgram, List<Integer> mentor,
            String keyword, int page) {
        page = Math.min(0, page - 1);
        PageRequest pageable = PageRequest.of(page, 10);

        // Kiểm tra null vì Hibernate không coi List rỗng là null
        if (internshipProgram == null || internshipProgram.isEmpty()) {
            internshipProgram = null;
        }
        if (mentor == null || mentor.isEmpty()) {
            mentor = null;
        }

        Page<Team> teams = teamRepository.searchTeam(internshipProgram, mentor, keyword, pageable);

        List<GetAllTeamResponse> responses = teams.stream()
                .map(team -> {
                    // Map các field trùng tự động
                    GetAllTeamResponse res = modelMapper.map(team, GetAllTeamResponse.class);

                    // Map thủ công các field đặc biệt
                    res.setInternshipProgramName(team.getInternshipProgram().getName());
                    res.setMentorName(team.getMentor().getUser().getFullName());

                    return res;
                })
                .collect(Collectors.toList());

        return new PagedResponse<>(
                responses,
                page + 1,
                teams.getTotalElements(),
                teams.getTotalPages(),
                teams.hasNext(),
                teams.hasPrevious());
    }

}
