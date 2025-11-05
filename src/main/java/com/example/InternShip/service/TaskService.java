package com.example.InternShip.service;

import com.example.InternShip.dto.request.CreateTaskRequest;
import com.example.InternShip.dto.request.UpdateTaskRequest;
import com.example.InternShip.dto.response.TaskResponse;

import java.util.List;

public interface TaskService {
    TaskResponse createTask(CreateTaskRequest request);
    TaskResponse updateTask(Long taskId, UpdateTaskRequest request);
    void deleteTask(Long taskId);
    List<TaskResponse> getTasksBySprint(Long sprintId);
    List<TaskResponse> getTasksByIntern(Integer internId);
}
