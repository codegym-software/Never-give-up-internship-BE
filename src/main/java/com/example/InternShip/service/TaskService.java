package com.example.InternShip.service;

import com.example.InternShip.dto.request.CreateTaskRequest;
import com.example.InternShip.dto.request.UpdateTaskRequest;
import com.example.InternShip.dto.response.TaskResponse;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.InternShip.entity.enums.TaskStatus;

public interface TaskService {
    TaskResponse createTask(CreateTaskRequest request);
    TaskResponse updateTask(Long taskId, UpdateTaskRequest request);
    void deleteTask(Long taskId);
    Page<TaskResponse> getTasksBySprint(Long sprintId, TaskStatus status, Integer internId, Pageable pageable);
    List<TaskResponse> getTasksByIntern(Integer internId);
    TaskResponse getTaskById(Long taskId);
}
