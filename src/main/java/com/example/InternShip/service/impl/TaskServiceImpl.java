package com.example.InternShip.service.impl;

import com.example.InternShip.dto.request.CreateTaskRequest;
import com.example.InternShip.dto.request.UpdateTaskRequest;
import com.example.InternShip.dto.response.TaskResponse;
import com.example.InternShip.entity.Intern;
import com.example.InternShip.entity.Mentor;
import com.example.InternShip.entity.Sprint;
import com.example.InternShip.entity.Task;
import com.example.InternShip.entity.Team;
import com.example.InternShip.entity.User;
import com.example.InternShip.entity.enums.Role;
import com.example.InternShip.entity.enums.TaskStatus;
import com.example.InternShip.exception.ErrorCode;
import com.example.InternShip.exception.UserNotFoundException;
import com.example.InternShip.repository.InternRepository;
import com.example.InternShip.repository.MentorRepository;
import com.example.InternShip.repository.SprintRepository;
import com.example.InternShip.repository.TaskRepository;
import com.example.InternShip.service.AuthService;
import com.example.InternShip.service.TaskService;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
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
    private final AuthService authService;

    @Override
    public TaskResponse createTask(CreateTaskRequest request) {
        User user = authService.getUserLogin();
        try {
            if (!user.getRole().equals(Role.INTERN) && !user.getRole().equals(Role.MENTOR)) {
                throw new RuntimeException(ErrorCode.VERIFICATION_FAILED.getMessage());
            }

            Sprint sprint = sprintRepository.findById(request.getSprintId())
                    .orElseThrow(() -> new RuntimeException("Sprint not found"));
            if (user.getRole().equals(Role.INTERN)) {
                Intern intern = internRepository.findByUser(user)
                        .orElseThrow(() -> new UserNotFoundException("Intern not found"));

                if (!sprint.getTeam().getId().equals(intern.getTeam().getId())) {
                    throw new RuntimeException("You are not in this team");
                }
            }
            if (user.getRole().equals(Role.MENTOR)) {
                Mentor mentor = mentorRepository.findByUser(user)
                        .orElseThrow(() -> new UserNotFoundException("Mentor not found"));
                if (mentor.getTeams().stream().noneMatch(team -> team.getId().equals(sprint.getTeam().getId()))) {
                    throw new RuntimeException("You are not in this team");
                }
            }

            Intern intern = internRepository.findById(request.getInternId())
                    .orElseThrow(() -> new UserNotFoundException("Intern not found"));

            Mentor mentor = mentorRepository.findByUser(user)
                    .orElseThrow(() -> new UserNotFoundException("Mentor not found"));

            Task task = new Task();
            task.setName(request.getName());
            task.setDescription(request.getDescription());
            task.setSprint(sprint);
            task.setIntern(intern);
            task.setMentor(mentor);
            task.setDeadline(request.getDeadline() == null ? sprint.getEndDate() : request.getDeadline());
            task.setStatus(TaskStatus.TODO);
            Task savedTask = taskRepository.save(task);
            return modelMapper.map(savedTask, TaskResponse.class);
        } catch (Exception e) {
            // TODO: handle exception
            throw new RuntimeException(e);
        }

    }

    @Override
    public TaskResponse updateTask(Long taskId, UpdateTaskRequest request) {
        User user = authService.getUserLogin();
        ;
        try {
            if (!user.getRole().equals(Role.INTERN) && !user.getRole().equals(Role.MENTOR)) {
                throw new RuntimeException(ErrorCode.VERIFICATION_FAILED.getMessage());
            }
            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new RuntimeException("Task not found"));
            Sprint sprint = sprintRepository.findById(task.getSprint().getId())
                    .orElseThrow(() -> new RuntimeException("Sprint not found"));
            if (user.getRole().equals(Role.INTERN)) {
                Intern intern = internRepository.findByUser(user)
                        .orElseThrow(() -> new UserNotFoundException("Intern not found"));
                if (!sprint.getTeam().getId().equals(intern.getTeam().getId())) {
                    throw new RuntimeException("You are not in this team");
                }
            }

            if (user.getRole().equals(Role.MENTOR)) {
                Mentor mentor = mentorRepository.findByUser(user)
                        .orElseThrow(() -> new UserNotFoundException("Mentor not found"));
                if (mentor.getTeams().stream().noneMatch(team -> team.getId().equals(sprint.getTeam().getId()))) {
                    throw new RuntimeException("You are not in this team");
                }

            }

            task.setName(request.getName());
            task.setDescription(request.getDescription());
            task.setStatus(request.getStatus());
            task.setDeadline(request.getDeadline());

            Task updatedTask = taskRepository.save(task);
            return modelMapper.map(updatedTask, TaskResponse.class);
        } catch (Exception e) {
            // TODO: handle exception
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteTask(Long taskId) {
        User user = authService.getUserLogin();
        if (!user.getRole().equals(Role.INTERN) && !user.getRole().equals(Role.MENTOR)) {
            throw new RuntimeException(ErrorCode.VERIFICATION_FAILED.getMessage());
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Sprint sprint = task.getSprint();

        if (user.getRole().equals(Role.INTERN)) {
            Intern intern = internRepository.findByUser(user)
                    .orElseThrow(() -> new UserNotFoundException("Intern not found"));
            if (!sprint.getTeam().getId().equals(intern.getTeam().getId())) {
                throw new RuntimeException("You are not in this team");
            }
        }

        if (user.getRole().equals(Role.MENTOR)) {
            Mentor mentor = mentorRepository.findByUser(user)
                    .orElseThrow(() -> new UserNotFoundException("Mentor not found"));
            if (mentor.getTeams().stream().noneMatch(team -> team.getId().equals(sprint.getTeam().getId()))) {
                throw new RuntimeException("You are not the mentor of this team");
            }
        }

        taskRepository.delete(task);
    }

    @Override
    public Page<TaskResponse> getTasksBySprint(Long sprintId, TaskStatus status, Integer internId, Pageable pageable) {
        Specification<Task> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("sprint").get("id"), sprintId));

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            if (internId != null) {
                predicates.add(criteriaBuilder.equal(root.get("intern").get("id"), internId));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Task> tasksPage = taskRepository.findAll(spec, pageable);
        return tasksPage.map(task -> modelMapper.map(task, TaskResponse.class));
    }

    @Override
    public List<TaskResponse> getTasksByIntern(Integer internId) {
        List<Task> tasks = taskRepository.findByInternId(internId);
        return tasks.stream()
                .map(task -> modelMapper.map(task, TaskResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public TaskResponse getTaskById(Long taskId) {
        User user = authService.getUserLogin();
        if (!user.getRole().equals(Role.INTERN) && !user.getRole().equals(Role.MENTOR)) {
            throw new SecurityException("You do not have permission to view this task.");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Sprint sprint = task.getSprint();

        if (user.getRole().equals(Role.INTERN)) {
            Intern intern = internRepository.findByUser(user)
                    .orElseThrow(() -> new UserNotFoundException("Intern not found"));
            if (!sprint.getTeam().getId().equals(intern.getTeam().getId())) {
                throw new SecurityException("You are not a member of the team that owns this task.");
            }
        }

        if (user.getRole().equals(Role.MENTOR)) {
            Mentor mentor = mentorRepository.findByUser(user)
                    .orElseThrow(() -> new UserNotFoundException("Mentor not found"));
            if (mentor.getTeams().stream().noneMatch(team -> team.getId().equals(sprint.getTeam().getId()))) {
                throw new SecurityException("You are not the mentor of the team that owns this task.");
            }
        }

        return modelMapper.map(task, TaskResponse.class);
    }
}
