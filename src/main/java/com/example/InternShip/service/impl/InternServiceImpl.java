package com.example.InternShip.service.impl;

import com.example.InternShip.dto.request.GetAllInternRequest;
import com.example.InternShip.dto.response.GetInternResponse;
import com.example.InternShip.dto.response.PagedResponse;
import com.example.InternShip.entity.Intern;
import com.example.InternShip.repository.InternRepository;
import com.example.InternShip.service.InternService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InternServiceImpl implements InternService {
    private final InternRepository internRepository;
    private final ModelMapper modelMapper;

    public PagedResponse<GetInternResponse> getAllIntern (GetAllInternRequest request){
        int page = Math.max(0, request.getPage() - 1);
        int size = 15;
        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Intern> interns = internRepository.searchInterns(request.getMajorId(),request.getUniversityId(),request.getKeyWord(),pageable);

        List<GetInternResponse> response = interns.map(intern ->{
                GetInternResponse dto = modelMapper.map(intern.getUser(),GetInternResponse.class);
                modelMapper.map(intern,dto);
                dto.setMajor(intern.getMajor().getName());
                dto.setUniversity(intern.getUniversity().getName());
                return dto;
        }).getContent();

        return new PagedResponse<>(
                response,
                page + 1,
                interns.getTotalElements(),
                interns.getTotalPages(),
                interns.hasNext(),
                interns.hasPrevious()
        );
    }
}
