package com.example.InternShip.controller;

import com.example.InternShip.dto.allowancepackage.request.CreateAllowancePackageRequest;
import com.example.InternShip.dto.allowancepackage.response.AllowancePackageResponse;
import com.example.InternShip.service.AllowancePackageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AllowancePackageController.class)
class AllowancePackageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AllowancePackageService allowancePackageService;

    @Test
    void whenGetAllowancePackageById_thenReturnPackage() throws Exception {
        // Arrange
        AllowancePackageResponse response = new AllowancePackageResponse();
        response.setId(1);
        response.setName("Basic Allowance");

        when(allowancePackageService.getAllowancePackageById(1)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/v1/hr/allowance-packages/1").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Basic Allowance"));
    }

    @Test
    void whenCreateAllowancePackage_thenReturnCreatedPackage() throws Exception {
        // Arrange
        CreateAllowancePackageRequest request = new CreateAllowancePackageRequest();
        request.setName("New Package");
        request.setAmount(new BigDecimal("1000.00"));
        request.setRequiredWorkDays(20);
        request.setInternshipProgramId(1);

        AllowancePackageResponse response = new AllowancePackageResponse();
        response.setId(2);
        response.setName("New Package");

        when(allowancePackageService.createAllowancePackage(any(CreateAllowancePackageRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/hr/allowance-packages").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Package"));
    }

    @Test
    void whenUpdateAllowancePackage_thenReturnUpdatedPackage() throws Exception {
        // Arrange
        CreateAllowancePackageRequest request = new CreateAllowancePackageRequest();
        request.setName("Updated Package");
        request.setAmount(new BigDecimal("1200.00"));
        request.setRequiredWorkDays(22);
        request.setInternshipProgramId(1);

        AllowancePackageResponse response = new AllowancePackageResponse();
        response.setId(1);
        response.setName("Updated Package");

        when(allowancePackageService.updateAllowancePackage(eq(1), any(CreateAllowancePackageRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(put("/api/v1/hr/allowance-packages/1").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Package"));
    }

    @Test
    void whenDeleteAllowancePackage_thenSucceed() throws Exception {
        // Arrange
        doNothing().when(allowancePackageService).deleteAllowancePackage(1);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/hr/allowance-packages/1").with(jwt()))
                .andExpect(status().isNoContent());
    }
}
