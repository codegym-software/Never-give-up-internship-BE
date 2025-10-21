package com.example.InternShip.repository;

import com.example.InternShip.dto.request.CreateMentorRequest;
import com.example.InternShip.dto.response.MentorResponse;
import com.example.InternShip.entity.Mentor;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MentorRepository extends JpaRepository<Mentor, Integer> {

      @Query("""
          SELECT m
          FROM Mentor m
          JOIN m.user u
          JOIN m.department d
          WHERE (:department IS NULL OR d.id IN :department)
            AND (
                  :keyword IS NULL
                  OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  OR u.phone LIKE CONCAT('%', :keyword, '%')
                )
      """)
  Page<Mentor> searchMentor(
      @Param("department") List<Integer> department,
      @Param("keyword") String keyword,
      Pageable pageable);
}
