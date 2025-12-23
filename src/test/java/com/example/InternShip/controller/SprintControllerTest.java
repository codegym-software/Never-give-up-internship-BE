package com.example.InternShip.controller;

import com.example.InternShip.dto.sprint.request.CreateSprintRequest;
import com.example.InternShip.dto.sprint.request.UpdateSprintRequest;
import com.example.InternShip.dto.sprint.response.SprintResponse;
import com.example.InternShip.service.SprintService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SprintController.class)
class SprintControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SprintService sprintService;

    @Test
    void whenGetSprintById_thenReturnSprint() throws Exception {
        // Arrange
        SprintResponse response = new SprintResponse();
        response.setId(1L);
        response.setName("Test Sprint");

        when(sprintService.getSprintById(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/v1/sprints/1").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Sprint"));
    }

    @Test
    void whenCreateSprint_thenReturnNewSprint() throws Exception {
        // Arrange
        CreateSprintRequest request = new CreateSprintRequest();
        request.setName("New Sprint");
        request.setStartDate(LocalDate.now());
        request.setEndDate(LocalDate.now().plusDays(14));

        SprintResponse response = new SprintResponse();
        response.setId(2L);
        response.setName("New Sprint");

        when(sprintService.createSprint(eq(1), any(CreateSprintRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/teams/1/sprints").with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Sprint"));
    }

    @Test
    void whenUpdateSprint_thenReturnUpdatedSprint() throws Exception {
        // Arrange
        UpdateSprintRequest request = new UpdateSprintRequest();
        request.setName("Updated Sprint Name");

        SprintResponse response = new SprintResponse();
        response.setId(1L);
        response.setName("Updated Sprint Name");

        when(sprintService.updateSprint(eq(1L), any(UpdateSprintRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(put("/api/v1/sprints/1").with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Sprint Name"));
    }

    @Test
    void whenDeleteSprint_thenSucceed() throws Exception {
        // Arrange
        doNothing().when(sprintService).deleteSprint(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/sprints/1").with(jwt()))
                .andExpect(status().isOk());
    }
}
