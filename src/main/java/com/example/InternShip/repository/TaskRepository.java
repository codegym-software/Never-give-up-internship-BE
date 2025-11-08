package com.example.InternShip.repository;

import com.example.InternShip.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    List<Task> findBySprintId(Long sprintId);
    List<Task> findByAssigneeId(Integer assigneeId);
}
