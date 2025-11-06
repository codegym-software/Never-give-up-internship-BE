package com.example.InternShip.repository;

import com.example.InternShip.entity.Sprint;
import com.example.InternShip.entity.Task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, Long> {
    Page<Sprint> findByTeamId(Integer teamId, Pageable pageable);
    Optional<Sprint> findById(Long id);
    List<Task> getTasksById(Long sprintId);
}
