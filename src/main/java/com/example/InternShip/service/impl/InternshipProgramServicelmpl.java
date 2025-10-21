package com.example.InternShip.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.example.InternShip.dto.response.GetAllInternProgramManagerResponse;
import com.example.InternShip.dto.response.GetAllInternProgramResponse;
import com.example.InternShip.dto.response.PagedResponse;
import com.example.InternShip.entity.enums.Role;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.InternShip.entity.InternshipProgram;
import com.example.InternShip.repository.InternshipProgramRepository;
import com.example.InternShip.service.InternshipProgramService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InternshipProgramServicelmpl implements InternshipProgramService {
    private final InternshipProgramRepository internshipProgramRepository;
    private final AuthServiceImpl authService;
    private final ModelMapper modelMapper;

    @Override
    public List<GetAllInternProgramResponse> getAllPrograms() {
        Role role = authService.getUserLogin().getRole();
        List<InternshipProgram> results = null;
        if (role.equals(Role.VISITOR)) {
            results = internshipProgramRepository.findAllByStatus(InternshipProgram.Status.PUBLISHED);
        } else {
            results = internshipProgramRepository.findAll();
        }
        return results.stream()
                .map(result -> modelMapper.map(result, GetAllInternProgramResponse.class))
                .toList();
    }

    @Override // Tùng
    public PagedResponse<GetAllInternProgramManagerResponse> getAllInternshipPrograms(List<Integer> department,
            String keyword, int page) {
        page = Math.min(0, page - 1);
        PageRequest pageable = PageRequest.of(page, 10);

        // Kiểm tra null vì Hibernate không coi List rỗng là null
        if (department == null || department.isEmpty()) {
            department = null;
        }

        Page<InternshipProgram> internshipPrograms = internshipProgramRepository.searchInternshipProgram(department,
                keyword, pageable);

        List<GetAllInternProgramManagerResponse> responses = internshipPrograms.stream()
                .map(internshipProgram -> modelMapper.map(internshipProgram, GetAllInternProgramManagerResponse.class))
                .collect(Collectors.toList());

        return new PagedResponse<>(
                responses,
                page + 1,
                internshipPrograms.getTotalElements(),
                internshipPrograms.getTotalPages(),
                internshipPrograms.hasNext(),
                internshipPrograms.hasPrevious());
    }
}
