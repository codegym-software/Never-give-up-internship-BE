package com.example.InternShip.service.impl;

import com.example.InternShip.dto.request.AddMemberRequest;
import com.example.InternShip.dto.request.CreateTeamRequest;
import com.example.InternShip.dto.response.GetAllTeamResponse;
import com.example.InternShip.dto.response.GetInternResponse;
import com.example.InternShip.dto.response.PagedResponse;
import com.example.InternShip.dto.response.TeamDetailResponse;
import com.example.InternShip.entity.*;
import com.example.InternShip.exception.ErrorCode;
import com.example.InternShip.repository.InternRepository;
import com.example.InternShip.repository.InternshipProgramRepository;
import com.example.InternShip.repository.MentorRepository;
import com.example.InternShip.repository.TeamRepository;
import com.example.InternShip.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final InternRepository internRepository;
    private final InternshipProgramRepository programRepository;
    private final MentorRepository mentorRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public TeamDetailResponse createTeam(CreateTeamRequest request) {
        InternshipProgram program = programRepository.findById(request.getInternshipProgramId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.PROGRAM_NOT_EXISTED.getMessage()));

        Mentor mentor = mentorRepository.findById(request.getMentorId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MENTOR_NOT_EXISTED.getMessage()));

        if (teamRepository.existsByNameAndInternshipProgram(request.getName(), program)) {
            throw new IllegalArgumentException(ErrorCode.TEAM_NAME_EXISTED.getMessage());
        }

        Team newTeam = new Team();
        newTeam.setName(request.getName());
        newTeam.setInternshipProgram(program);
        newTeam.setMentor(mentor);

        Team savedTeam = teamRepository.save(newTeam);

        return mapToTeamDetailResponse(savedTeam);
    }

    @Override
    public TeamDetailResponse getTeamDetails(Integer teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.TEAM_NOT_EXISTED.getMessage()));
        return mapToTeamDetailResponse(team);
    }

    @Override
    public TeamDetailResponse addMember(Integer teamId, AddMemberRequest request) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.TEAM_NOT_EXISTED.getMessage()));

        List<Intern> internsToUpdate = new ArrayList<>();

        for (Integer internId : request.getInternIds()) {
            Intern intern = internRepository.findById(internId)
                    .orElseThrow(() -> new EntityNotFoundException(ErrorCode.INTERN_NOT_EXISTED.getMessage()));

            if (intern.getTeam() != null) {
                throw new IllegalStateException(ErrorCode.INTERN_INVALID.getMessage());
            }

            intern.setTeam(team);
            internsToUpdate.add(intern);
        }
        internRepository.saveAll(internsToUpdate);

        Team updatedTeam = teamRepository.findById(teamId).get();
        return mapToTeamDetailResponse(updatedTeam);
    }

    @Override
    @Transactional
    public void removeMember(Integer internId) {
        Intern intern = internRepository.findById(internId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.INTERN_NOT_EXISTED.getMessage()));
        intern.setTeam(null);
        internRepository.save(intern);
    }

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

    private TeamDetailResponse mapToTeamDetailResponse(Team team) {
        TeamDetailResponse response = new TeamDetailResponse();
        response.setId(team.getId());
        response.setTeamName(team.getName());
        response.setInternshipProgramName(team.getInternshipProgram().getName());

        Mentor mentor = team.getMentor();
        if (mentor != null) {
            User mentorUser = mentor.getUser();
            if (mentorUser != null) {
                response.setMentorName(mentorUser.getFullName());
            }
        }

        List<GetInternResponse> members = team.getInterns().stream()
                .map(this::mapToInternResponse)
                .collect(Collectors.toList());
        response.setMembers(members);

        return response;
    }

    private GetInternResponse mapToInternResponse(Intern intern) {
        GetInternResponse dto = modelMapper.map(intern.getUser(), GetInternResponse.class);
        modelMapper.map(intern, dto);
        dto.setMajor(intern.getMajor().getName());
        dto.setUniversity(intern.getUniversity().getName());

        return dto;
    }
}
