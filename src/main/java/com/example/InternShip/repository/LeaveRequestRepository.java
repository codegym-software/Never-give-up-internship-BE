package com.example.InternShip.repository;

import com.example.InternShip.entity.Intern;
import com.example.InternShip.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest,Integer> {
    // Tìm đơn nghỉ phép (đã được duyệt) của 1 intern, trong 1 ngày cụ thể
    Optional<LeaveRequest> findByInternAndDateAndApproved(Intern intern, LocalDate date, Boolean approved);
}
