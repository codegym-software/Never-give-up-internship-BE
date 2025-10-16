package com.example.InternShip.repository;

import com.example.InternShip.entity.InternshipProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InternshipProgramRepository extends JpaRepository<InternshipProgram, Integer> {
    List<InternshipProgram> findAllByStatus(InternshipProgram.Status status);
}

