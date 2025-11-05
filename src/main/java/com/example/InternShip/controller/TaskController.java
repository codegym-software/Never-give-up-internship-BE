package com.example.InternShip.controller;

import com.example.InternShip.dto.request.CreateTaskRequest;
import com.example.InternShip.dto.request.UpdateTaskRequest;
import com.example.InternShip.dto.response.ApiResponse;
import com.example.InternShip.dto.response.TaskResponse;
import com.example.InternShip.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/sprints/{sprintId}/tasks")
    public ResponseEntity<ApiResponse> createTask(@PathVariable Long sprintId, @RequestBody CreateTaskRequest request) {
        request.setSprintId(sprintId);
        TaskResponse taskResponse = taskService.createTask(request);
        ApiResponse response = new ApiResponse(HttpStatus.CREATED.value(), "Task created successfully", taskResponse);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/sprints/{sprintId}/tasks")
    public ResponseEntity<ApiResponse> getTasksBySprint(@PathVariable Long sprintId) {
        List<TaskResponse> tasks = taskService.getTasksBySprint(sprintId);
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
}
