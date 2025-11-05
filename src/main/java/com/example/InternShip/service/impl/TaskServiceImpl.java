package com.example.InternShip.service.impl;

import com.example.InternShip.dto.request.CreateTaskRequest;
import com.example.InternShip.dto.request.UpdateTaskRequest;
import com.example.InternShip.dto.response.TaskResponse;
import com.example.InternShip.entity.Intern;
import com.example.InternShip.entity.Mentor;
import com.example.InternShip.entity.Sprint;
import com.example.InternShip.entity.Task;
import com.example.InternShip.entity.enums.TaskStatus;
import com.example.InternShip.exception.UserNotFoundException;
import com.example.InternShip.repository.InternRepository;
import com.example.InternShip.repository.MentorRepository;
import com.example.InternShip.repository.SprintRepository;
import com.example.InternShip.repository.TaskRepository;
import com.example.InternShip.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final SprintRepository sprintRepository;
    private final InternRepository internRepository;
    private final MentorRepository mentorRepository;
    private final ModelMapper modelMapper;

    @Override
    public TaskResponse createTask(CreateTaskRequest request) {
        Sprint sprint = sprintRepository.findById(request.getSprintId())
                .orElseThrow(() -> new RuntimeException("Sprint not found"));
        Intern intern = internRepository.findById(request.getInternId())
                .orElseThrow(() -> new UserNotFoundException("Intern not found"));
        Mentor mentor = mentorRepository.findById(request.getMentorId())
                .orElseThrow(() -> new UserNotFoundException("Mentor not found"));

        Task task = new Task();
        task.setName(request.getName());
        task.setDescription(request.getDescription());
        task.setSprint(sprint);
        task.setIntern(intern);
        task.setMentor(mentor);
        task.setDeadline(request.getDeadline());
        task.setStatus(TaskStatus.TODO);

        Task savedTask = taskRepository.save(task);
        return modelMapper.map(savedTask, TaskResponse.class);
    }

    @Override
    public TaskResponse updateTask(Long taskId, UpdateTaskRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setName(request.getName());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setDeadline(request.getDeadline());

        Task updatedTask = taskRepository.save(task);
        return modelMapper.map(updatedTask, TaskResponse.class);
    }

    @Override
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    @Override
    public List<TaskResponse> getTasksBySprint(Long sprintId) {
        List<Task> tasks = taskRepository.findBySprintId(sprintId);
        return tasks.stream()
                .map(task -> modelMapper.map(task, TaskResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponse> getTasksByIntern(Integer internId) {
        List<Task> tasks = taskRepository.findByInternId(internId);
        return tasks.stream()
                .map(task -> modelMapper.map(task, TaskResponse.class))
                .collect(Collectors.toList());
    }
}
