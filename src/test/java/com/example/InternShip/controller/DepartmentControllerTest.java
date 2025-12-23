package com.example.InternShip.controller;

import com.example.InternShip.dto.department.response.GetAllDepartmentResponse;
import com.example.InternShip.service.DepartmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DepartmentController.class)
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentService departmentService;

    @Test
    void whenGetAllDepartments_thenReturnDepartmentList() throws Exception {
        // 1. Arrange
        GetAllDepartmentResponse departmentResponse = new GetAllDepartmentResponse(1, "Human Resources");
        List<GetAllDepartmentResponse> departmentList = Collections.singletonList(departmentResponse);

        when(departmentService.getAllDepartments()).thenReturn(departmentList);

        // 2. Act & Assert
        mockMvc.perform(get("/api/v1/departments").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Human Resources"));
    }
}
