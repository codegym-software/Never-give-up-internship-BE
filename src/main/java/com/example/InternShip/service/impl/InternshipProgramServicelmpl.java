package com.example.InternShip.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.InternShip.entity.InternshipProgram;
import com.example.InternShip.repository.InternshipProgramRepository;
import com.example.InternShip.service.InternshipProgramService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InternshipProgramServicelmpl implements InternshipProgramService{

    private final InternshipProgramRepository internshipProgramRepository;

    @Override
    public List<InternshipProgram> getAllPrograms() {
        List<InternshipProgram> result = internshipProgramRepository.findAll();
        return result.stream()
                .filter(program -> program.getStatus() == InternshipProgram.Status.PUBLISHED)
                .toList();
    }
    
}
