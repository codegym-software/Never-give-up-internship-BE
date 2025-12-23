package com.example.InternShip.controller;

import com.example.InternShip.dto.workSchedule.request.CreateWorkScheduleRequest;
import com.example.InternShip.dto.workSchedule.request.UpdateWorkScheduleRequest;
import com.example.InternShip.dto.workSchedule.response.WorkScheduleResponse;
import com.example.InternShip.service.WorkScheduleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorkScheduleController.class)
class WorkScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WorkScheduleService workScheduleService;

    @Test
    void whenGetWorkSchedule_thenReturnSchedule() throws Exception {
        // Arrange
        WorkScheduleResponse response = new WorkScheduleResponse();
        response.setId(1);
        response.setDayOfWeek(DayOfWeek.MONDAY);
        when(workScheduleService.getWorkSchedule(1)).thenReturn(Collections.singletonList(response));

        // Act & Assert
        mockMvc.perform(get("/api/v1/workSchedules/1").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dayOfWeek").value("MONDAY"));
    }

    @Test
    void whenCreateSchedule_thenReturnNewSchedule() throws Exception {
        // Arrange
        CreateWorkScheduleRequest request = new CreateWorkScheduleRequest();
        request.setIdTeam(1);
        request.setDayOfWeek(DayOfWeek.TUESDAY);
        request.setTimeStart(LocalTime.of(9, 0));
        request.setTimeEnd(LocalTime.of(17, 0));

        WorkScheduleResponse response = new WorkScheduleResponse();
        response.setId(2);
        response.setDayOfWeek(DayOfWeek.TUESDAY);

        when(workScheduleService.createSchedule(any(CreateWorkScheduleRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/workSchedules").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dayOfWeek").value("TUESDAY"));
    }

    @Test
    void whenUpdateSchedule_thenReturnUpdatedSchedule() throws Exception {
        // Arrange
        UpdateWorkScheduleRequest request = new UpdateWorkScheduleRequest();
        request.setTimeStart(LocalTime.of(10, 0));
        request.setTimeEnd(LocalTime.of(18, 0));

        WorkScheduleResponse response = new WorkScheduleResponse();
        response.setId(1);
        response.setTimeStart(LocalTime.of(10, 0));

        when(workScheduleService.updateSchedule(eq(1), any(UpdateWorkScheduleRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(put("/api/v1/workSchedules/1").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timeStart").value("10:00:00"));
    }

    @Test
    void whenDeleteSchedule_thenSucceed() throws Exception {
        // Arrange
        doNothing().when(workScheduleService).deleteSchedule(1);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/workSchedules/1").with(jwt()))
                .andExpect(status().isOk());
    }
}
