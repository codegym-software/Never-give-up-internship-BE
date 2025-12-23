package com.example.InternShip.controller;

import com.example.InternShip.dto.internshipProgram.request.CreateInternProgramRequest;
import com.example.InternShip.dto.internshipProgram.request.UpdateInternProgramRequest;
import com.example.InternShip.service.InternshipProgramService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InternshipProgramController.class)
class InternshipProgramControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private InternshipProgramService internshipProgramService;

        @Test
        void whenCreateInternProgram_thenSucceed() throws Exception {
                // Arrange
                CreateInternProgramRequest request = new CreateInternProgramRequest(
                                "Spring Internship 2026",
                                LocalDateTime.now().plusDays(10),
                                LocalDateTime.now().plusDays(20),
                                LocalDateTime.now().plusDays(30),
                                1,
                                false);

                when(internshipProgramService.createInternProgram(any(CreateInternProgramRequest.class)))
                                .thenReturn(java.util.Collections.emptyMap());

                // Act & Assert
                mockMvc.perform(post("/api/v1/internship-programs").with(jwt())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk());
        }

        @Test
        void whenUpdateInternProgram_thenSucceed() throws Exception {
                // Arrange
                UpdateInternProgramRequest request = new UpdateInternProgramRequest(
                                "Updated Spring Internship 2026",
                                LocalDateTime.now().plusDays(11),
                                LocalDateTime.now().plusDays(21),
                                LocalDateTime.now().plusDays(31));

                when(internshipProgramService.updateInternProgram(any(UpdateInternProgramRequest.class), eq(1)))
                                .thenReturn(java.util.Collections.emptyMap());

                // Act & Assert
                mockMvc.perform(put("/api/v1/internship-programs/1").with(jwt())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk());
        }
}
