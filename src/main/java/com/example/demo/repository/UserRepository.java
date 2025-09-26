package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    @Query("""
        SELECT u FROM User u
        WHERE (:role IS NULL OR u.role.roleName = :role)
          AND (:keyword IS NULL OR 
              LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
              LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
              CAST(u.id AS string) LIKE CONCAT('%', :keyword, '%')
          )
    """)
    Page<User> searchUsers(String role, String keyword, Pageable pageable);
}
