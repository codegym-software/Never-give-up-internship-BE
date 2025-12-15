package com.example.InternShip.controller;

import com.example.InternShip.dto.response.UniversityGetAllResponse;
import com.example.InternShip.entity.University;
import com.example.InternShip.repository.UniversityRepository;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UniversityController.class)
class UniversityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UniversityRepository universityRepository;

    @MockBean
    private ModelMapper modelMapper;

    @Test
    void whenGetAllUniversity_thenReturnUniversityList() throws Exception {
        // Arrange
        University university = new University();
        university.setId(1);
        university.setName("FPT University");

        UniversityGetAllResponse universityResponse = new UniversityGetAllResponse(1, "FPT University");

        when(universityRepository.findAll()).thenReturn(Collections.singletonList(university));
        when(modelMapper.map(any(University.class), eq(UniversityGetAllResponse.class))).thenReturn(universityResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/universities").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("FPT University"));
    }
}
