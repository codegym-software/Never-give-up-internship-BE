package com.example.InternShip.service;

import java.util.List;

import com.example.InternShip.dto.response.GetAllInternProgramResponse;

public interface InternshipProgramService {
    
    public List<GetAllInternProgramResponse> getAllPrograms();
}
