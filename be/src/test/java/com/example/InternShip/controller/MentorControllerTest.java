package com.example.InternShip.controller;

import com.example.InternShip.dto.mentor.request.CreateMentorRequest;
import com.example.InternShip.dto.mentor.request.UpdateMentorRequest;
import com.example.InternShip.dto.mentor.response.GetMentorResponse;
import com.example.InternShip.service.MentorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MentorController.class)
class MentorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MentorService mentorService;

    @Test
    void whenCreateMentor_thenSucceed() throws Exception {
        // Arrange
        CreateMentorRequest request = new CreateMentorRequest();
        request.setFullName("New Mentor");
        request.setEmail("mentor@example.com");
        request.setDepartmentId(1);

        GetMentorResponse response = new GetMentorResponse();
        response.setId(1);
        response.setFullName("New Mentor");
        response.setEmail("mentor@example.com");

        when(mentorService.createMentor(any(CreateMentorRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/mentors").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("mentor@example.com"));
    }

    @Test
    void whenUpdateMentor_thenSucceed() throws Exception {
        // Arrange
        UpdateMentorRequest request = new UpdateMentorRequest();
        request.setDepartmentId(2);

        GetMentorResponse response = new GetMentorResponse();
        response.setId(1);
        response.setDepartmentName("New Department");

        when(mentorService.updateMentorDepartment(eq(1), any(UpdateMentorRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(put("/api/v1/mentors/1").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departmentName").value("New Department"));
    }
}
