package com.example.demo.repository;

import com.example.demo.entity.EmailToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailTokenRepository extends JpaRepository<EmailToken, Integer> {
    Optional<EmailToken> findByToken(String token);
}
