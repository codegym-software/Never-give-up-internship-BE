package com.example.InternShip.repository;

import com.example.InternShip.entity.InternshipApplication;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InternshipApplicationRepository extends JpaRepository<InternshipApplication, Integer> {
    boolean existsByUserId(Integer userId);
    Optional<InternshipApplication> findByUserId(Integer userId);
}
