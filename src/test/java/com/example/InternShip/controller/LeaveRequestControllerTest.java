package com.example.InternShip.controller;

import com.example.InternShip.dto.leaveRequest.request.RejectLeaveApplicationRequest;
import com.example.InternShip.dto.leaveRequest.response.GetAllLeaveApplicationResponse;
import com.example.InternShip.dto.leaveRequest.response.GetLeaveApplicationResponse;
import com.example.InternShip.service.LeaveRequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LeaveRequestController.class)
class LeaveRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LeaveRequestService leaveRequestService;

    @Test
    void whenViewLeaveApplication_thenSucceed() throws Exception {
        // Arrange
        GetLeaveApplicationResponse response = new GetLeaveApplicationResponse();
        response.setInternName("Test Intern");
        when(leaveRequestService.viewLeaveApplication(1)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/v1/leaveRequests/1").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.internName").value("Test Intern"));
    }

    @Test
    void whenCancelLeaveApplication_thenSucceed() throws Exception {
        // Arrange
        doNothing().when(leaveRequestService).cancelLeaveApplication(1);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/leaveRequests/1").with(jwt()))
                .andExpect(status().isOk());
    }

    @Test
    void whenApproveLeaveApplication_thenSucceed() throws Exception {
        // Arrange
        GetAllLeaveApplicationResponse response = new GetAllLeaveApplicationResponse();
        response.setId(1);
        response.setApproved(true);
        when(leaveRequestService.approveLeaveApplication(1)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(patch("/api/v1/leaveRequests/approve/1").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.approved").value(true));
    }

    @Test
    void whenRejectLeaveApplication_thenSucceed() throws Exception {
        // Arrange
        RejectLeaveApplicationRequest request = new RejectLeaveApplicationRequest();
        request.setReasonReject("Not enough leave balance.");

        GetAllLeaveApplicationResponse response = new GetAllLeaveApplicationResponse();
        response.setId(1);
        response.setApproved(false);
        response.setReasonReject("Not enough leave balance.");

        when(leaveRequestService.rejectLeaveApplication(eq(1), any(RejectLeaveApplicationRequest.class)))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(patch("/api/v1/leaveRequests/reject/1").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.approved").value(false));
    }
}
