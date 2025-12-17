package com.example.InternShip.service;


import com.example.InternShip.dto.AllowanceResponse;
import com.example.InternShip.dto.request.AllowanceRequest;
import com.example.InternShip.dto.response.PagedResponse;
import org.springframework.data.domain.Pageable;

public interface AllowanceService {
    PagedResponse<AllowanceResponse> getAllAllowances(Long internshipProgramId, String keyword, String status, Pageable pageable);
    AllowanceResponse transferAllowance(long id);
    PagedResponse<AllowanceResponse> getMyAllowances( Pageable pageable);
    AllowanceResponse createAllowance(AllowanceRequest request);
    void cancelAllowance(long id);
}

