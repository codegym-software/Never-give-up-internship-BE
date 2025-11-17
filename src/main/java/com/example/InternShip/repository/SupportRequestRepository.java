package com.example.InternShip.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.InternShip.entity.SupportRequest;

public interface SupportRequestRepository extends JpaRepository<SupportRequest, Integer> {

    @Query("""
            SELECT sr
            FROM SupportRequest sr
            JOIN sr.intern i
            JOIN i.user u
            WHERE
                (:keyword IS NULL OR :keyword = '' OR
                    LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
                )
            AND
                (:status IS NULL OR sr.status = :status)
            """)
    List<SupportRequest> searchSupportRequest(String keyword, SupportRequest.Status status);

}
