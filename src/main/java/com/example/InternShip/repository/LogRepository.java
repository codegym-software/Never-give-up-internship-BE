package com.example.InternShip.repository;

import com.example.InternShip.entity.Log;
import com.example.InternShip.entity.Log.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface LogRepository extends JpaRepository<Log, Integer> {
    @Query("SELECT l FROM Log l " +
            "LEFT JOIN l.action u " +
            "WHERE (:fromDate IS NULL OR l.actionAt >= :fromDate) " +
            "AND (:toDate IS NULL OR l.actionAt <= :toDate) " +
            "AND (:affected IS NULL OR l.affected = :affected) " +
            "AND (:keyword IS NULL OR lower(u.fullName) LIKE lower(concat('%', :keyword, '%')))")
    Page<Log> searchLogs(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            @Param("affected") Model affected,
            @Param("keyword") String keyword, //tên người thực hiện
            Pageable pageable);
}
