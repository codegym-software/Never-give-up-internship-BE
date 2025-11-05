package com.example.InternShip.repository;

import com.example.InternShip.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findBySprintId(Long sprintId);
    List<Task> findByInternId(Integer internId);
}
