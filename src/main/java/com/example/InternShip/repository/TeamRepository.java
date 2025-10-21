package com.example.InternShip.repository;

import com.example.InternShip.entity.InternshipProgram;
import com.example.InternShip.entity.Team;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamRepository extends JpaRepository<Team, Integer> {
    boolean existsByNameAndInternshipProgram(String name, InternshipProgram program);
    // Tùng
    @Query("""
        SELECT t
        FROM Team t
        LEFT JOIN t.internshipProgram ip
        LEFT JOIN t.mentor m
        WHERE
            (:internshipProgram IS NULL OR ip.id IN :internshipProgram)
            AND
            (:mentor IS NULL OR m.id IN :mentor)
            AND (
            :keyword IS NULL OR :keyword = ''
            OR LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
        """)
        Page<Team> searchTeam(
            @Param("internshipProgram") List<Integer> internshipProgram,
            @Param("mentor") List<Integer> mentor,
            @Param("keyword") String keyword,
            Pageable pageable);

    Team findByMentor_id(Integer id);
}
