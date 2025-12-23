package com.example.InternShip.controller;

import com.example.InternShip.dto.user.request.CreateUserRequest;
import com.example.InternShip.dto.user.request.UpdateUserRequest;
import com.example.InternShip.dto.user.response.GetUserResponse;
import com.example.InternShip.entity.enums.Role;
import com.example.InternShip.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void whenGetUserInfo_thenReturnUserInfo() throws Exception {
        // Arrange
        GetUserResponse response = new GetUserResponse();
        response.setId(1);
        response.setEmail("test@example.com");
        response.setRole(Role.INTERN);

        when(userService.getUserInfo()).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/info").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void whenCreateUser_withAdminRole_thenSucceed() throws Exception {
        // Arrange
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("newuser@example.com");
        request.setFullName("New User");
        request.setRole("HR");

        GetUserResponse response = new GetUserResponse();
        response.setId(2);
        response.setEmail("newuser@example.com");
        response.setRole(Role.HR);

        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/users")
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("newuser@example.com"));
    }

    @Test
    void whenUpdateUser_withHrRole_thenSucceed() throws Exception {
        // Arrange
        UpdateUserRequest request = new UpdateUserRequest();
        request.setActive(true);
        request.setRole("INTERN");

        GetUserResponse response = new GetUserResponse();
        response.setId(3);
        response.setActive(true);
        response.setRole(Role.INTERN);

        when(userService.updateUser(any(UpdateUserRequest.class), eq(3))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(put("/api/v1/users/3")
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_HR")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(true));
    }
}
