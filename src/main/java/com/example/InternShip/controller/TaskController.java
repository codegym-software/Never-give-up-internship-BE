package com.example.InternShip.controller;

import com.example.InternShip.dto.request.CreateTaskRequest;
import com.example.InternShip.dto.request.UpdateTaskRequest;
import com.example.InternShip.dto.response.ApiResponse;
import com.example.InternShip.dto.response.TaskResponse;
import com.example.InternShip.service.AuthService;
import com.example.InternShip.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.InternShip.entity.enums.TaskStatus;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final AuthService authService;
    @PostMapping("/sprints/{sprintId}/tasks")
    public ResponseEntity<ApiResponse> createTask(@PathVariable Long sprintId, @RequestBody CreateTaskRequest request) {
        request.setSprintId(sprintId);
        TaskResponse taskResponse = taskService.createTask(request);
        ApiResponse response = new ApiResponse(HttpStatus.CREATED.value(), "Task created successfully", taskResponse);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/sprints/{sprintId}/tasks")
    public ResponseEntity<ApiResponse> getTasksBySprint(
            @PathVariable Long sprintId,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) Integer internId,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<TaskResponse> tasks = taskService.getTasksBySprint(sprintId, status, internId, pageable);
        ApiResponse response = new ApiResponse(HttpStatus.OK.value(), "Tasks retrieved successfully", tasks);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/interns/{internId}/tasks")
    public ResponseEntity<ApiResponse> getTasksByIntern(@PathVariable Integer internId) {
        List<TaskResponse> tasks = taskService.getTasksByIntern(internId);
        ApiResponse response = new ApiResponse(HttpStatus.OK.value(), "Tasks retrieved successfully", tasks);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<ApiResponse> updateTask(@PathVariable Long taskId, @RequestBody UpdateTaskRequest request) {
        TaskResponse taskResponse = taskService.updateTask(taskId, request);
        ApiResponse response = new ApiResponse(HttpStatus.OK.value(), "Task updated successfully", taskResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<ApiResponse> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        ApiResponse response = new ApiResponse(HttpStatus.OK.value(), "Task deleted successfully", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<ApiResponse> getTaskById(@PathVariable Long taskId) {
        TaskResponse taskResponse = taskService.getTaskById(taskId);
        ApiResponse response = new ApiResponse(HttpStatus.OK.value(), "Task retrieved successfully", taskResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
