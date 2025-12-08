package com.example.InternShip.controller;

import com.example.InternShip.dto.task.request.CreateTaskRequest;
import com.example.InternShip.dto.task.request.UpdateTaskRequest;
import com.example.InternShip.dto.task.response.TaskResponse;
import com.example.InternShip.entity.enums.TaskStatus;
import com.example.InternShip.service.TaskService;
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

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @Test
    void whenGetTaskById_thenReturnTask() throws Exception {
        // Arrange
        TaskResponse response = new TaskResponse();
        response.setId(1L);
        response.setName("Test Task");

        when(taskService.getTaskById(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/v1/tasks/1").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Task"));
    }

    @Test
    void whenCreateTask_thenReturnNewTask() throws Exception {
        // Arrange
        CreateTaskRequest request = new CreateTaskRequest();
        request.setName("New Task");
        request.setDescription("A new task description");

        TaskResponse response = new TaskResponse();
        response.setId(2L);
        response.setName("New Task");
        response.setStatus(TaskStatus.TODO);

        when(taskService.createTask(any(CreateTaskRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/sprints/1/tasks").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Task"));
    }

    @Test
    void whenUpdateTask_thenReturnUpdatedTask() throws Exception {
        // Arrange
        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setName("Updated Task Name");
        request.setStatus(TaskStatus.IN_PROGRESS);

        TaskResponse response = new TaskResponse();
        response.setId(1L);
        response.setName("Updated Task Name");
        response.setStatus(TaskStatus.IN_PROGRESS);

        when(taskService.updateTask(eq(1L), any(UpdateTaskRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(put("/api/v1/tasks/1").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void whenDeleteTask_thenSucceed() throws Exception {
        // Arrange
        doNothing().when(taskService).deleteTask(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/tasks/1").with(jwt()))
                .andExpect(status().isOk());
    }
}
