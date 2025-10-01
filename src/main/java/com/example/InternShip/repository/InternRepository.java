package com.example.InternShip.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.InternShip.entity.Intern;

public interface InternRepository extends JpaRepository<Intern,Integer> {

    Optional<Intern> findAllById(Integer id);
    
}
