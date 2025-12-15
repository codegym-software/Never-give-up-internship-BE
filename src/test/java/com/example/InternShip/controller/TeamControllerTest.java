package com.example.InternShip.controller;

import com.example.InternShip.dto.team.request.CreateTeamRequest;
import com.example.InternShip.dto.team.request.UpdateTeamRequest;
import com.example.InternShip.dto.team.response.TeamDetailResponse;
import com.example.InternShip.service.TeamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeamController.class)
class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TeamService teamService;

    @Test
    void whenGetTeamById_thenReturnTeamDetail() throws Exception {
        // Arrange
        TeamDetailResponse response = new TeamDetailResponse();
        response.setId(1);
        response.setTeamName("The A-Team");

        when(teamService.getTeamById(1)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/v1/teams/1").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.teamName").value("The A-Team"));
    }

    @Test
    void whenCreateTeam_thenReturnNewTeamDetail() throws Exception {
        // Arrange
        CreateTeamRequest request = new CreateTeamRequest();
        request.setName("New Team");
        request.setInternshipProgramId(1);
        request.setMentorId(1);

        TeamDetailResponse response = new TeamDetailResponse();
        response.setId(2);
        response.setTeamName("New Team");
        response.setMembers(new ArrayList<>());

        when(teamService.createTeam(any(CreateTeamRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/teams").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.teamName").value("New Team"));
    }

    @Test
    void whenUpdateTeam_thenReturnUpdatedTeamDetail() throws Exception {
        // Arrange
        UpdateTeamRequest request = new UpdateTeamRequest();
        request.setName("Updated Team Name");
        request.setMentorId(2);

        TeamDetailResponse response = new TeamDetailResponse();
        response.setId(1);
        response.setTeamName("Updated Team Name");
        response.setMentorName("New Mentor");

        when(teamService.updateTeam(eq(1), any(UpdateTeamRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(put("/api/v1/teams/1").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.teamName").value("Updated Team Name"));
    }
}
