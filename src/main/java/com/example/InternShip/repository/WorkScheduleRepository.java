package com.example.InternShip.repository;

import com.example.InternShip.entity.Team;
import com.example.InternShip.entity.WorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.Optional;

public interface WorkScheduleRepository extends JpaRepository<WorkSchedule,Integer> {
    // Tìm lịch làm việc của 1 nhóm, vào 1 ngày cụ thể trong tuần
    Optional<WorkSchedule> findByTeamAndDayOfWeek(Team team, DayOfWeek dayOfWeek);
}
