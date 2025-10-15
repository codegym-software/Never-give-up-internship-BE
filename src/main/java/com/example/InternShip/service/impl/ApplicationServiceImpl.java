package com.example.InternShip.service.impl;

import com.example.InternShip.dto.request.ApplicationRequest;
import com.example.InternShip.dto.request.SubmitApplicationContractRequest;
import com.example.InternShip.dto.response.ApplicationResponse;
import com.example.InternShip.dto.response.FileResponse;
import com.example.InternShip.dto.response.PagedResponse;
import com.example.InternShip.entity.*;
import com.example.InternShip.exception.ErrorCode;
import com.example.InternShip.repository.*;
import com.example.InternShip.service.ApplicationService;
import com.example.InternShip.service.CloudinaryService;
import java.util.stream.Collectors;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

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
                List<InternshipApplication> liststatus = applicationRepository.findAllByUserId(user.getId());
                boolean hasActiveApplication = liststatus.stream()
                                .anyMatch(app -> app.getStatus().equals("SUBMITTED") ||
                                                app.getStatus().equals("APPROVED") ||
                                                app.getStatus().equals("CONFIRM"));

                if (hasActiveApplication) {
                        throw new RuntimeException(
                                        "Bạn đã có đơn ứng tuyển đang chờ xử lý hoặc đã được duyệt. Vui lòng không nộp đơn mới.");
                }
                // if (applicationRepository.existsByUserId(user.getId())) {
                // throw new RuntimeException(ErrorCode.APPLICATION_EXISTED.getMessage());
                // }

                // Kiểm tra và lấy thông tin từ các ID trong request
                InternshipProgram program = programRepository.findById(request.getInternshipTermId())
                                .orElseThrow(() -> new EntityNotFoundException(
                                                ErrorCode.INTERNSHIPTERM_NOT_EXISTED.getMessage()));
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
                applicationRepository.save(application);
                return mapToApplicationResponse(application);
        }

        // Nam
        @Override
        @Transactional(readOnly = true)
        public List<ApplicationResponse> getMyApplication() {
                // Lấy user hiện tại (đã đăng nhập)
                User user = authService.getUserLogin();

                // Lấy tất cả đơn ứng tuyển của user đó
                List<InternshipApplication> listApplication = applicationRepository.findAllByUserId(user.getId());
                
                // Ánh xạ từng InternshipApplication sang ApplicationResponse DTO
                return listApplication.stream()
                                .map(this::mapToApplicationResponse)
                                .toList();
        }

        @Override
        @Transactional(readOnly = true)
        public PagedResponse<ApplicationResponse> getAllApplication(Integer internshipTerm, Integer university,
                        Integer major, String keyword, String status, int page) {
                page = Math.min(0, page - 1);
                PageRequest pageable = PageRequest.of(page, 10);
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

                // Upload file CV lên Cloudinary
                FileResponse fileResponse = cloudinaryService.uploadFile(request.getApplicationContractFile(),
                                "Submit Application Contract");
                // Lấy url file
                application.setInternshipContractUrl(fileResponse.getFileUrl());
                // Chuyển trạng thái đơn xin thực tập
                application.setStatus(InternshipApplication.Status.CONFIRM);
                // Lưu vào csdl
                applicationRepository.save(application);
        }

        // Phương thức ánh xạ cho phương thức lấy thông tin đơn xin thực tập
        public ApplicationResponse mapToApplicationResponse(InternshipApplication app) {
                User user = app.getUser();
                ApplicationResponse res = modelMapper.map(user, ApplicationResponse.class);
                modelMapper.map(app, res);
                res.setInternshipApplicationStatus(app.getStatus().name());
                res.setInternshipProgram(app.getInternshipProgram().getId().toString());
                res.setUniversityName(app.getUniversity().getName());
                res.setMajorName(app.getMajor().getName());
                return res;
        }

        @Override
        public void withdrawApplication(Integer applicationId) { // rút đơn

                try {
                        User user = authService.getUserLogin();
                        InternshipApplication application = applicationRepository.findById(applicationId)
                                        .orElseThrow(() -> new EntityNotFoundException(
                                                        ErrorCode.INTERNSHIP_APPLICATION_NOT_EXISTED.getMessage()));
                        if (user.getId() != application.getUser().getId()) {
                                throw new IllegalArgumentException(ErrorCode.UNAUTHORIZED_ACTION.getMessage());
                        }
                        application.setStatus(InternshipApplication.Status.WITHDRAWN);
                        applicationRepository.save(application);
                } catch (Exception e) {
                        // TODO: handle exception
                        throw new RuntimeException(ErrorCode.WITHDRAWAL_FAILED.getMessage());
                }
        }

}