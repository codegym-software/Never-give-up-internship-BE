package com.example.InternShip.repository;

import com.example.InternShip.dto.attendance.response.GetAllAttendanceResponse;
import com.example.InternShip.entity.Attendance;
import com.example.InternShip.entity.Intern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    // Tìm bản ghi chấm công của 1 intern, trong 1 ngày cụ thể
    Optional<Attendance> findByInternAndDate(Intern intern, LocalDate date);

    List<Attendance> findAllByTeamId(int teamId);

}
