package com.example.InternShip.repository;

import com.example.InternShip.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest,Integer> {
}
