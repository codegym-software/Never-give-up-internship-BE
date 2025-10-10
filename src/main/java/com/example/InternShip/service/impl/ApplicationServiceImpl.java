package com.example.InternShip.service.impl;

import com.example.InternShip.dto.request.ApplicationRequest;
import com.example.InternShip.dto.response.ApplicationResponse;
import com.example.InternShip.dto.response.FileResponse;
import com.example.InternShip.entity.*;
import com.example.InternShip.exception.ErrorCode;
import com.example.InternShip.repository.*;
import com.example.InternShip.service.ApplicationService;
import com.example.InternShip.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final InternshipApplicationRepository applicationRepository;
    private final InternshipProgramRepository programRepository;
    private final UniversityRepository universityRepository;
    private final MajorRepository majorRepository;
    private final AuthServiceImpl authService;
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional
    public ApplicationResponse submitApplication(ApplicationRequest request) {
        User applicant = authService.getUserLogin();

        if (applicationRepository.existsByUserId(applicant.getId())) {
            throw new RuntimeException(ErrorCode.APPLICATION_EXISTED.getMessage());
        }

        // Kiểm tra và lấy thông tin từ các ID trong request
        InternshipProgram program = programRepository.findById(request.getInternshipTermId())
                .orElseThrow(() -> new RuntimeException(ErrorCode.INTERNSHIPTERM_NOT_EXISTED.getMessage()));
        University university = universityRepository.findById(request.getUniversityId())
                .orElseThrow(() -> new RuntimeException(ErrorCode.UNIVERSITY_NOT_EXISTED.getMessage()));
        Major major = majorRepository.findById(request.getMajorId())
                .orElseThrow(() -> new RuntimeException(ErrorCode.MAJOR_NOT_EXISTED.getMessage()));

        // Upload file CV lên Cloudinary
        FileResponse fileResponse = cloudinaryService.uploadFile(request.getCvFile());

        // Tạo và lưu đơn ứng tuyển
        InternshipApplication application = new InternshipApplication();
        application.setUser(applicant);
        application.setInternshipProgram(program);
        application.setUniversity(university);
        application.setMajor(major);
        application.setCvUrl(fileResponse.getFileUrl());
        application.setStatus(InternshipApplication.Status.SUBMITTED);

        InternshipApplication savedApplication = applicationRepository.save(application);

        // Xây dựng và trả về DTO response
        return ApplicationResponse.builder()
                .applicationId(savedApplication.getId())
                .internshipTerm("Kì thực tập " + program.getTimeStart() + " - " + program.getTimeEnd())
                .university(university.getName())
                .major(major.getName())
                .applicantName(applicant.getFullName())
                .status(savedApplication.getStatus())
                .appliedDate(savedApplication.getCreatedAt())
                .message("Nộp đơn ứng tuyển thành công!")
                .build();
    }

@Override
@Transactional(readOnly = true)
public ApplicationResponse getMyApplication() {
    User applicant = authService.getUserLogin();

    return applicationRepository.findByUserId(applicant.getId())
            .map(app -> {
                InternshipProgram program = app.getInternshipProgram();
                University university = app.getUniversity();
                Major major = app.getMajor();

                return ApplicationResponse.builder()
                        .applicationId(app.getId())
                        .internshipTerm(program != null
                                ? "Kì thực tập " + program.getTimeStart() + " - " + program.getTimeEnd()
                                : "")
                        .university(university != null ? university.getName() : "")
                        .major(major != null ? major.getName() : "")
                        .applicantName(applicant.getFullName())
                        .status(app.getStatus())
                        .appliedDate(app.getCreatedAt())
                        .message("OK")
                        .build();
            })
            .orElse(null); // trả về null nếu chưa nộp
}
}