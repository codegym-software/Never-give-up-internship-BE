package com.example.InternShip.service.impl;

import com.example.InternShip.dto.request.UpdateInfoRequest;
import com.example.InternShip.dto.request.UpdateInternRequest;
import com.example.InternShip.entity.Intern;
import com.example.InternShip.entity.Major;
import com.example.InternShip.entity.University;
import com.example.InternShip.exception.ErrorCode;
import com.example.InternShip.repository.InternRepository;
import com.example.InternShip.repository.MajorRepository;
import com.example.InternShip.repository.UniversityRepository;
import com.example.InternShip.service.InternService;
import lombok.RequiredArgsConstructor;

import java.io.ObjectInputFilter.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InternServiceImpl implements InternService {

    private final InternRepository internRepository;

    private final UniversityRepository universityRepository;

    private final MajorRepository majorRepository;

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
                    .orElseThrow(() -> new RuntimeException(ErrorCode.MAJOR_NOTE_EXITED.getMessage()));
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

}
