package com.example.InternShip.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.InternShip.entity.Allowance;

public interface AllowanceRepository extends JpaRepository<Allowance, Integer> {
    
}
