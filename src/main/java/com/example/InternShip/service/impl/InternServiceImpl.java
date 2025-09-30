package com.example.InternShip.service.impl;

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
import com.example.InternShip.service.InternService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InternServiceImpl implements InternService {

    private final UserRepository userRepository;
    private final InternRepository internRepository;
    private final UniversityRepository universityRepository;
    private final MajorRepository majorRepository;
    private final ModelMapper modelMapper;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Override
    @Transactional
    public InternResponse createIntern(CreateInternRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException(ErrorCode.EMAIL_EXISTED.getMessage());
        }
        University university = universityRepository.findById(request.getUniversityId())
                .orElseThrow(()-> new RuntimeException(ErrorCode.UNIVERSITY_NOT_EXISTED.getMessage()));
        Major major = majorRepository.findById(request.getMajorId())
                .orElseThrow(()-> new RuntimeException(ErrorCode.MAJOR_NOT_EXISTED.getMessage()));

        User user = modelMapper.map(request, User.class);
        user.setFullName(request.getFullName());
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
}