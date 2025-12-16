package com.example.InternShip.repository;

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
    @Query("SELECT COUNT(a) FROM Attendance a " +
           "WHERE a.intern.id = :internId " +
           "AND a.date BETWEEN :startDate AND :endDate " +
           "AND a.checkIn IS NOT NULL AND a.checkOut IS NOT NULL " +
           "AND a.checkIn <= a.timeStart " +
           "AND a.checkOut >= a.timeEnd")
    long countWorkDays(@Param("internId") Integer internId,
                       @Param("startDate") LocalDate startDate,
                       @Param("endDate") LocalDate endDate);

}
