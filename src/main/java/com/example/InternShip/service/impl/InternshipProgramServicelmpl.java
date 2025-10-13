package com.example.InternShip.service.impl;

import java.util.List;

import com.example.InternShip.dto.response.GetAllInternProgram;
import com.example.InternShip.entity.enums.Role;
import com.example.InternShip.service.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.InternShip.entity.InternshipProgram;
import com.example.InternShip.repository.InternshipProgramRepository;
import com.example.InternShip.service.InternshipProgramService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InternshipProgramServicelmpl implements InternshipProgramService {
    private final ModelMapper modelMapper;
    private final InternshipProgramRepository internshipProgramRepository;
    private final AuthServiceImpl authService;

    @Override
    public List<GetAllInternProgram> getAllPrograms() {
        Role role = authService.getUserLogin().getRole();
        List<InternshipProgram> results = null;
        if (role.equals(Role.VISITOR)) {
            results = internshipProgramRepository.findAllByStatus(InternshipProgram.Status.PUBLISHED);
        }else {
            results = internshipProgramRepository.findAll();
        }
        return results.stream()
                .map(result -> modelMapper.map(result,GetAllInternProgram.class))
                .toList();
    }
}
