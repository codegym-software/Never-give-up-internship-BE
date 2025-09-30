package com.example.InternShip.repository;

import com.example.InternShip.entity.Intern;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternRepository extends JpaRepository<Intern, Integer> {
    
}
