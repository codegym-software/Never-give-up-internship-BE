package com.example.InternShip.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.InternShip.entity.SupportRequest;

public interface SupportRequestRepository extends JpaRepository<SupportRequest, Integer>{
    
}
