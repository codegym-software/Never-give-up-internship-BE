package com.example.InternShip.controller;

import com.example.InternShip.dto.response.MajorGetAllResponse;
import com.example.InternShip.entity.Major;
import com.example.InternShip.repository.MajorRepository;
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

@WebMvcTest(MajorController.class)
class MajorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MajorRepository majorRepository;

    @MockBean
    private ModelMapper modelMapper;

    @Test
    void whenGetAllMajor_thenReturnMajorList() throws Exception {
        // Arrange
        Major major = new Major();
        major.setId(1);
        major.setName("Software Engineering");

        MajorGetAllResponse majorResponse = new MajorGetAllResponse(1, "Software Engineering");

        when(majorRepository.findAll()).thenReturn(Collections.singletonList(major));
        when(modelMapper.map(any(Major.class), eq(MajorGetAllResponse.class))).thenReturn(majorResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/majors").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Software Engineering"));
    }
}
