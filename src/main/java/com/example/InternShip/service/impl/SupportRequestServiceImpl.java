package com.example.InternShip.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.InternShip.dto.cloudinary.response.FileResponse;
import com.example.InternShip.dto.supportRequest.request.CreateSupportRequestRequest;
import com.example.InternShip.dto.supportRequest.request.RejectSupportRequestRequest;
import com.example.InternShip.dto.supportRequest.response.GetSupportRequestReponse;
import com.example.InternShip.entity.SupportRequest;
import com.example.InternShip.entity.User;
import com.example.InternShip.exception.ErrorCode;
import com.example.InternShip.repository.SupportRequestRepository;
import com.example.InternShip.service.AuthService;
import com.example.InternShip.service.CloudinaryService;
import com.example.InternShip.service.SupportRequestService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupportRequestServiceImpl implements SupportRequestService {
    private final SupportRequestRepository supportRequestRepository;

    private final CloudinaryService cloudinaryService;
    private final AuthService authService;

    private final ModelMapper modelMapper;

    @Override
    public GetSupportRequestReponse createSupportRequest(CreateSupportRequestRequest request) {
        User user = authService.getUserLogin();
        SupportRequest supportRequest = new SupportRequest();
        supportRequest.setIntern(user.getIntern());
        supportRequest.setTitle(request.getTitle());
        supportRequest.setDescription(request.getDescription());
        if (request.getEvidenceFile() != null) {
            FileResponse fileResponse = cloudinaryService.uploadFile(request.getEvidenceFile(), "Evidence");
            supportRequest.setEvidenceFile(fileResponse.getFileUrl());
        }
        supportRequestRepository.save(supportRequest);
        GetSupportRequestReponse res = modelMapper.map(supportRequest, GetSupportRequestReponse.class);
        res.setEvidenceFileUrl(supportRequest.getEvidenceFile());
        res.setInternName(supportRequest.getIntern().getUser().getFullName());
        res.setInternEmail(supportRequest.getIntern().getUser().getEmail());
        res.setHandlerName(supportRequest.getHandler().getFullName());
        return res;
    }

    @Override
    public List<GetSupportRequestReponse> getAllSupportRequest(String keyword, String status) {
        SupportRequest.Status statusEnum = status == null ? null : SupportRequest.Status.valueOf(status);

        List<SupportRequest> supportRequests = supportRequestRepository.searchSupportRequest(keyword, statusEnum);
        return supportRequests.stream().map(supportRequest -> {
            GetSupportRequestReponse res = modelMapper.map(supportRequest, GetSupportRequestReponse.class);
            res.setEvidenceFileUrl(supportRequest.getEvidenceFile());
            res.setInternName(supportRequest.getIntern().getUser().getFullName());
            res.setInternEmail(supportRequest.getIntern().getUser().getFullName());
            res.setHandlerName(supportRequest.getHandler().getFullName());
            return res;
        }).toList();
    }

    @Override
    public GetSupportRequestReponse approveSupportRequest(Integer supportId) {
        // K biết là có cần check hr đảm nhiệm k nx nhưng mà cứ để tạm như này :3
        SupportRequest supportRequest = supportRequestRepository.findById(supportId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.SUPPORT_REQUEST_NOT_EXISTS.getMessage()));
        // PENDING cũng duyệt được nên là làm như sau
        if (supportRequest.getStatus() == SupportRequest.Status.REJECTED) {
            throw new IllegalArgumentException(ErrorCode.SUPPORT_REQUEST_STATUS_INVALID.getMessage());
        }
        supportRequest.setStatus(SupportRequest.Status.RESOLVED);
        supportRequestRepository.save(supportRequest);
        GetSupportRequestReponse res = modelMapper.map(supportRequest, GetSupportRequestReponse.class);
        res.setEvidenceFileUrl(supportRequest.getEvidenceFile());
        res.setInternName(supportRequest.getIntern().getUser().getFullName());
        res.setInternEmail(supportRequest.getIntern().getUser().getEmail());
        res.setHandlerName(supportRequest.getHandler().getFullName());
        return res;
    }

    @Override
    public GetSupportRequestReponse inProgressSupportRequest(Integer supportId) {
        SupportRequest supportRequest = supportRequestRepository.findById(supportId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.SUPPORT_REQUEST_NOT_EXISTS.getMessage()));
        if (supportRequest.getStatus() == SupportRequest.Status.PENDING) {
            throw new IllegalArgumentException(ErrorCode.SUPPORT_REQUEST_STATUS_INVALID.getMessage());
        }
        supportRequest.setStatus(SupportRequest.Status.IN_PROGRESS);
        supportRequestRepository.save(supportRequest);
        GetSupportRequestReponse res = modelMapper.map(supportRequest, GetSupportRequestReponse.class);
        res.setEvidenceFileUrl(supportRequest.getEvidenceFile());
        res.setInternName(supportRequest.getIntern().getUser().getFullName());
        res.setInternEmail(supportRequest.getIntern().getUser().getEmail());
        res.setHandlerName(supportRequest.getHandler().getFullName());
        return res;
    }

    @Override
    public GetSupportRequestReponse rejectSupportRequest(Integer supportId, RejectSupportRequestRequest request) {
        SupportRequest supportRequest = supportRequestRepository.findById(supportId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.SUPPORT_REQUEST_NOT_EXISTS.getMessage()));
        if (supportRequest.getStatus() == SupportRequest.Status.RESOLVED) {
            throw new IllegalArgumentException(ErrorCode.SUPPORT_REQUEST_STATUS_INVALID.getMessage());
        }
        supportRequest.setStatus(SupportRequest.Status.REJECTED);
        supportRequestRepository.save(supportRequest);
        GetSupportRequestReponse res = modelMapper.map(supportRequest, GetSupportRequestReponse.class);
        res.setEvidenceFileUrl(supportRequest.getEvidenceFile());
        res.setInternName(supportRequest.getIntern().getUser().getFullName());
        res.setInternEmail(supportRequest.getIntern().getUser().getEmail());
        res.setHandlerName(supportRequest.getHandler().getFullName());
        return res;
    }

    @Override
    public void cancelSupportRequest(Integer supportId) {
        SupportRequest supportRequest = supportRequestRepository.findById(supportId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.SUPPORT_REQUEST_NOT_EXISTS.getMessage()));
        if (supportRequest.getStatus() != SupportRequest.Status.PENDING) {
            throw new IllegalArgumentException(ErrorCode.SUPPORT_REQUEST_STATUS_INVALID.getMessage());
        }
        supportRequestRepository.delete(supportRequest);
    }

}
