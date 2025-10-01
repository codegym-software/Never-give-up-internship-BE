package com.example.InternShip.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.InternShip.entity.Major;

public interface MajorRepository extends JpaRepository<Major, Integer> {

   Optional< Major> findAllById(Integer majorId);
    
}
