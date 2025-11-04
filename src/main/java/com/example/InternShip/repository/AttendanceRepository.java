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

    List<Attendance> findByInternAndDateBetweenOrderByDateAsc(Intern intern, LocalDate startDate, LocalDate endDate);

    public interface AttendanceSummaryProjection {
        Integer getInternId();

        long getTotalWorkingDays();

        long getTotalOnLeaveDays();

        long getTotalAbsentDays();
    }

    @Query("SELECT " +
            "    a.intern.id as internId, " +
            "    SUM(CASE WHEN a.status IN ('PRESENT', 'LATE', 'EARLY_LEAVE', 'LATE_AND_EARLY_LEAVE') THEN 1 ELSE 0 END) as totalWorkingDays, " +
            "    SUM(CASE WHEN a.status = 'ON_LEAVE' THEN 1 ELSE 0 END) as totalOnLeaveDays, " +
            "    SUM(CASE WHEN a.status = 'ABSENT' THEN 1 ELSE 0 END) as totalAbsentDays " +
            "FROM Attendance a " +
            "WHERE a.date BETWEEN :startDate AND :endDate " +
            "AND (:teamId IS NULL OR a.team.id = :teamId) " +
            "GROUP BY a.intern.id")
    List<AttendanceSummaryProjection> getAttendanceSummary(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("teamId") Integer teamId);
}
