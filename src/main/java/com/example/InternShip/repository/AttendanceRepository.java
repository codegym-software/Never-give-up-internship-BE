package com.example.InternShip.repository;

import com.example.InternShip.entity.Attendance;
import com.example.InternShip.entity.Intern;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance,Integer> {
    // Tìm bản ghi chấm công của 1 intern, trong 1 ngày cụ thể
    Optional<Attendance> findByInternAndDate(Intern intern, LocalDate date);
}
