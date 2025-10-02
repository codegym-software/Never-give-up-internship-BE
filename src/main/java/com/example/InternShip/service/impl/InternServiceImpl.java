package com.example.InternShip.service.impl;

import com.example.InternShip.dto.request.UpdateInternRequest;
import com.example.InternShip.dto.request.CreateInternRequest;
import com.example.InternShip.dto.response.InternResponse;
import com.example.InternShip.entity.Intern;
import com.example.InternShip.entity.Major;
import com.example.InternShip.entity.University;
import com.example.InternShip.entity.User;
import com.example.InternShip.entity.enums.Role;
import com.example.InternShip.exception.ErrorCode;
import com.example.InternShip.repository.InternRepository;
import com.example.InternShip.repository.MajorRepository;
import com.example.InternShip.repository.UniversityRepository;
import com.example.InternShip.repository.UserRepository;
import com.example.InternShip.dto.request.GetAllInternRequest;
import com.example.InternShip.dto.response.GetInternResponse;
import com.example.InternShip.dto.response.PagedResponse;

import com.example.InternShip.service.InternService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.io.ObjectInputFilter.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InternServiceImpl implements InternService {

    private final UserRepository userRepository;
    private final InternRepository internRepository;
    private final UniversityRepository universityRepository;
    private final MajorRepository majorRepository;
    private final ModelMapper modelMapper;


    @Override
    public void updateIntern(Integer id, UpdateInternRequest updateInternRequest) {
        Intern intern = internRepository.findAllById(id)
                .orElseThrow(() -> new RuntimeException(ErrorCode.INTERN_NOT_EXISTED.getMessage()));

        if (updateInternRequest.getUniversityId() != null) {
            University university = universityRepository.findAllById(updateInternRequest.getUniversityId())
                    .orElseThrow(() -> new RuntimeException(ErrorCode.UNIVERSITY_NOT_EXISTED.getMessage()));

            intern.setUniversity(university);
        }

        if (updateInternRequest.getMajorId() != null) {
            Major major = majorRepository.findAllById(updateInternRequest.getMajorId())
                    .orElseThrow(() -> new RuntimeException(ErrorCode.MAJOR_NOT_EXISTED.getMessage()));
            intern.setMajor(major);
        }

        try {
            if (updateInternRequest.getStatus() != null) {
                intern.setStatus(Intern.Status.valueOf(updateInternRequest.getStatus().toUpperCase()));
            }
        } catch (Exception e) {
            throw new RuntimeException(ErrorCode.STATUS_INVALID.getMessage());

        }

        internRepository.save(intern);

    }


    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public InternResponse createIntern(CreateInternRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException(ErrorCode.EMAIL_EXISTED.getMessage());
        }
        University university = universityRepository.findById(request.getUniversityId())
                .orElseThrow(() -> new RuntimeException(ErrorCode.UNIVERSITY_NOT_EXISTED.getMessage()));
        Major major = majorRepository.findById(request.getMajorId())
                .orElseThrow(() -> new RuntimeException(ErrorCode.MAJOR_NOT_EXISTED.getMessage()));

        User user = modelMapper.map(request, User.class);
        user.setUsername(request.getEmail());
        user.setPassword(passwordEncoder.encode("123456@Abc"));
        user.setRole(Role.INTERN);
        User savedUser = userRepository.save(user);

        Intern intern = new Intern();
        intern.setUser(savedUser);
        intern.setStatus(Intern.Status.ACTIVE);
        intern.setMajor(major);
        intern.setUniversity(university);
        Intern savedIntern = internRepository.save(intern);

        InternResponse internResponse = modelMapper.map(savedUser, InternResponse.class);
        internResponse.setInternId(savedIntern.getId());
        internResponse.setMajorName(savedIntern.getMajor().getName());
        internResponse.setUniversityName(savedIntern.getUniversity().getName());
        internResponse.setStatus(savedIntern.getStatus());

        return internResponse;
    }


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

