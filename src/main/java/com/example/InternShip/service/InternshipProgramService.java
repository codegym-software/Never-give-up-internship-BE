package com.example.InternShip.service;

import java.util.List;

import com.example.InternShip.dto.response.GetAllInternProgramManagerResponse;
import com.example.InternShip.dto.response.GetAllInternProgramResponse;
import com.example.InternShip.dto.response.PagedResponse;

public interface InternshipProgramService {
    
    public List<GetAllInternProgramResponse> getAllPrograms();

    PagedResponse<GetAllInternProgramManagerResponse> getAllInternshipPrograms(List<Integer> department, String keyword, int page);
}
