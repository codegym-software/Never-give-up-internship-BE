package com.example.InternShip.service;

import java.util.List;

import com.example.InternShip.dto.response.GetAllInternProgramResponse;

public interface InternshipProgramService {
    
    public List<GetAllInternProgramResponse> getAllPrograms();
    public void endPublish (int programId);
    public void endReviewing (int programId);
    public void startInternship(int programId);
}
