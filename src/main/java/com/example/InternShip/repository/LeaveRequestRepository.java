package com.example.InternShip.repository;

import com.example.InternShip.entity.Intern;
import com.example.InternShip.entity.LeaveRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {
    // Tìm đơn nghỉ phép (đã được duyệt) của 1 intern, trong 1 ngày cụ thể
    Optional<LeaveRequest> findByInternAndDateAndApproved(Intern intern, LocalDate date, Boolean approved);

    @Query("""
            SELECT lr
            FROM LeaveRequest lr
            JOIN lr.intern i
            JOIN lr.hr u
            WHERE (:approved IS NULL OR lr.approved = :approved)
              AND (:type IS NULL OR lr.type = :type)
              AND (:keyword IS NULL OR :keyword = '' OR
                   LOWER(i.user.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<LeaveRequest> searchLeaveApplication(
            @Param("approved") Boolean approved,
            @Param("type") LeaveRequest.Type type,
            @Param("keyword") String keyword,
            Pageable pageable);

    List<LeaveRequest> findAllByApprovedFalse();
}
