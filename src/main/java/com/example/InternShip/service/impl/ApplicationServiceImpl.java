package com.example.InternShip.service.impl;

import com.example.InternShip.dto.request.ApplicationRequest;
import com.example.InternShip.dto.request.ApproveApplicationRequest;
import com.example.InternShip.dto.request.SubmitApplicationContractRequest;
import com.example.InternShip.dto.response.ApplicationResponse;
import com.example.InternShip.dto.response.FileResponse;
import com.example.InternShip.dto.response.PagedResponse;
import com.example.InternShip.entity.*;
import com.example.InternShip.entity.enums.Role;
import com.example.InternShip.exception.ErrorCode;
import com.example.InternShip.repository.*;
import com.example.InternShip.service.ApplicationService;
import com.example.InternShip.service.CloudinaryService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.modelmapper.ModelMapper;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final UserRepository userRepository;
    private final InternRepository internRepository;
    private final InternshipApplicationRepository applicationRepository;
    private final InternshipProgramRepository programRepository;
    private final UniversityRepository universityRepository;
    private final MajorRepository majorRepository;
    private final AuthServiceImpl authService;
    private final CloudinaryService cloudinaryService;

    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public ApplicationResponse submitApplication(ApplicationRequest request) { // Tài
        User user = authService.getUserLogin();

        if (applicationRepository.existsByUserId(user.getId())) {
            throw new RuntimeException(ErrorCode.APPLICATION_EXISTED.getMessage());
        }

        // Kiểm tra và lấy thông tin từ các ID trong request
        InternshipProgram program = programRepository.findById(request.getInternshipProgramId())
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorCode.INTERNSHIPPROGRAM_NOT_EXISTED.getMessage()));
        if (!program.getStatus().equals(InternshipProgram.Status.PUBLISHED)) {
            throw new IllegalArgumentException(ErrorCode.TIME_APPLY_INVALID.getMessage());
        }
        University university = universityRepository.findById(request.getUniversityId())
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorCode.UNIVERSITY_NOT_EXISTED.getMessage()));
        Major major = majorRepository.findById(request.getMajorId())
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorCode.MAJOR_NOT_EXISTED.getMessage()));

        // Upload file CV lên Cloudinary
        FileResponse cvFileRes = cloudinaryService.uploadFile(request.getCvFile(), "CV Files");
        // Upload file CV lên Cloudinary
        FileResponse internApplicationFileRes = cloudinaryService.uploadFile(request.getInternApplicationFile(),
                "Intern Application Files");

        // Tạo và lưu đơn ứng tuyển
        InternshipApplication application = new InternshipApplication();
        application.setUser(user);
        application.setInternshipProgram(program);
        application.setUniversity(university);
        application.setMajor(major);
        application.setCvUrl(cvFileRes.getFileUrl());
        application.setInternshipApplicationtUrl(internApplicationFileRes.getFileUrl());
        application.setStatus(InternshipApplication.Status.SUBMITTED);

        applicationRepository.save(application);

        return mapToApplicationResponse(application);
    }

    // Nam
    @Override
    @Transactional(readOnly = true)
    public ApplicationResponse getMyApplication() {
        User user = authService.getUserLogin();
        InternshipApplication internshipApplication = applicationRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.INTERNSHIP_APPLICATION_NOT_EXISTED.getMessage()));
        ApplicationResponse applicationResponse = mapToApplicationResponse(internshipApplication);
        return applicationResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ApplicationResponse> getAllApplication(Integer internshipTerm, Integer university,
                                                                Integer major, String keyword, String status, int page) {
        page = Math.min(0, page - 1);
        PageRequest pageable = PageRequest.of(page, 10, Sort.by("createdAt").descending());
        InternshipApplication.Status iStatus = parseInternshipApplicationStatus(status);
        Page<InternshipApplication> applications = applicationRepository.searchApplications(internshipTerm,
                university, major, keyword, iStatus, pageable);

        // Ánh xạ (map) danh sách Entity sang danh sách ApplicationResponse DTO
        List<ApplicationResponse> res = applications.map(this::mapToApplicationResponse)
                .getContent();

        return new PagedResponse<>(
                res,
                page + 1,
                applications.getTotalElements(),
                applications.getTotalPages(),
                applications.hasNext(),
                applications.hasPrevious());
    }

    private InternshipApplication.Status parseInternshipApplicationStatus(String status) {
        if (status != null && !status.isBlank()) {
            try {
                return InternshipApplication.Status.valueOf(status);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(ErrorCode.STATUS_INVALID.getMessage());
            }
        }
        return null;
    }

    // Tùng
    @Override
    public void submitApplicationContract(SubmitApplicationContractRequest request) { // Gửi hdtt
        User user = authService.getUserLogin();
        InternshipApplication application = applicationRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorCode.INTERNSHIP_APPLICATION_NOT_EXISTED.getMessage()));

        if (!application.getStatus().equals(InternshipApplication.Status.APPROVED)) {
            throw new IllegalArgumentException(ErrorCode.SUBMIT_FAILED.getMessage());
        }
        // Upload file HĐTT lên Cloudinary
        FileResponse fileResponse = cloudinaryService.uploadFile(request.getApplicationContractFile(),
                "Submit Application Contract");
        // Lấy url file
        application.setInternshipContractUrl(fileResponse.getFileUrl());
        // Chuyển trạng thái đơn xin thực tập
        application.setStatus(InternshipApplication.Status.CONFIRM);
        // Lưu vào csdl
        applicationRepository.save(application);
        //Kiểm tra xem User đã là Intern chưa để tránh tạo trùng
        if (internRepository.existsByUser(user)) {
            return;
        }
        //Tạo bản ghi Intern mới
        Intern intern = new Intern();
        intern.setUser(user);
        intern.setMajor(application.getMajor());
        intern.setUniversity(application.getUniversity());
        intern.setStatus(Intern.Status.ACTIVE);
        internRepository.save(intern);
        //Cập nhật vai trò
        user.setRole(Role.INTERN);
        userRepository.save(user);
    }

    // Phương thức ánh xạ cho phương thức lấy thông tin đơn xin thực tập
    public ApplicationResponse mapToApplicationResponse(InternshipApplication app) {
        User user = app.getUser();
        ApplicationResponse res = modelMapper.map(user, ApplicationResponse.class);
        modelMapper.map(app, res);
        res.setInternshipApplicationStatus(app.getStatus().name());
        res.setInternshipProgram(app.getInternshipProgram().getName());
        res.setUniversityName(app.getUniversity().getName());
        res.setMajorName(app.getMajor().getName());
        return res;
    }

    @Transactional //Duyệt đơn thực tập
    public void approveApplication(ApproveApplicationRequest request) {
        List<InternshipApplication> applications = applicationRepository.findAllById(request.getApplicationIds());
        for (InternshipApplication app : applications) {
            if (!app.getStatus().equals(InternshipApplication.Status.UNDER_REVIEW)) {
                throw new IllegalArgumentException(ErrorCode.STATUS_APPLICATION_INVALID.getMessage() + app.getUser().getEmail());
            }
            app.setStatus(InternshipApplication.Status.APPROVED);
        }
        applicationRepository.saveAll(applications);
    }

}