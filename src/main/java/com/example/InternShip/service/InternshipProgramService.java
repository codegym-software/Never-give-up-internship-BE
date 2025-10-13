package com.example.InternShip.service;

import java.util.List;

import com.example.InternShip.dto.response.GetAllInternProgram;
import com.example.InternShip.entity.InternshipProgram;

public interface InternshipProgramService {
    
    public List<GetAllInternProgram> getAllPrograms();
}
