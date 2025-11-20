package com.example.InternShip.service.impl;

import java.util.List;

import com.example.InternShip.dto.supportRequest.request.UpdateSupportRequestRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.InternShip.dto.cloudinary.response.FileResponse;
import com.example.InternShip.dto.supportRequest.request.CreateSupportRequestRequest;
import com.example.InternShip.dto.supportRequest.request.RejectSupportRequestRequest;
import com.example.InternShip.dto.supportRequest.response.GetSupportRequestResponse;
import com.example.InternShip.entity.SupportRequest;
import com.example.InternShip.entity.User;
import com.example.InternShip.exception.ErrorCode;
import com.example.InternShip.repository.SupportRequestRepository;
import com.example.InternShip.service.AuthService;
import com.example.InternShip.service.CloudinaryService;
import com.example.InternShip.service.SupportRequestService;
import com.example.InternShip.entity.SupportRequest.Status;

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

    //Intern gửi yêu cầu
    @Override
    public GetSupportRequestResponse createSupportRequest(CreateSupportRequestRequest request) {
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
        GetSupportRequestResponse res = modelMapper.map(supportRequest, GetSupportRequestResponse.class);
        res.setEvidenceFileUrl(supportRequest.getEvidenceFile());
        res.setInternName(supportRequest.getIntern().getUser().getFullName());
        res.setInternEmail(supportRequest.getIntern().getUser().getEmail());
        if (supportRequest.getHandler() != null) {
            res.setHandlerName(supportRequest.getHandler().getFullName());
        }
        return res;
    }

    //Intern lấy danh sách của chính mình
    @Override
    public List<GetSupportRequestResponse> getMyList() {
        User user = authService.getUserLogin();
        List<SupportRequest> list = supportRequestRepository.findAllByInternId(user.getIntern().getId());
        return list.stream().map(supportRequest -> {
            GetSupportRequestResponse res = modelMapper.map(supportRequest, GetSupportRequestResponse.class);
            res.setEvidenceFileUrl(supportRequest.getEvidenceFile());
            res.setInternName(supportRequest.getIntern().getUser().getFullName());
            res.setInternEmail(supportRequest.getIntern().getUser().getEmail());
            if (supportRequest.getHandler() != null) {
                res.setHandlerName(supportRequest.getHandler().getFullName());
            }
            return res;
        }).toList();
    }

    //Intern sửa yêu cầu
    @Override
    public GetSupportRequestResponse updateRequest(Integer id, UpdateSupportRequestRequest request) {
        User user = authService.getUserLogin();
        SupportRequest req = supportRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_SUPPORT_REQUEST.getMessage()));

        if (!req.getIntern().getId().equals(user.getIntern().getId())) {
            throw new IllegalArgumentException(ErrorCode.NOT_PERMISSION.getMessage());
        }

        if (req.getStatus() != SupportRequest.Status.PENDING) {
            throw new IllegalArgumentException("Chỉ được sửa khi yêu cầu chưa được xử lý");
        }

        // Cập nhật thông tin
        if (request.getTitle() != null) req.setTitle(request.getTitle());
        if (request.getDescription() != null) req.setDescription(request.getDescription());

        // Upload file mới nếu có
        if (request.getEvidenceFile() != null && !request.getEvidenceFile().isEmpty()) {
            FileResponse fileResponse = cloudinaryService.uploadFile(request.getEvidenceFile(), "Evidence");
            req.setEvidenceFile(fileResponse.getFileUrl());
        }

        supportRequestRepository.save(req);
        GetSupportRequestResponse res = modelMapper.map(req, GetSupportRequestResponse.class);
        res.setEvidenceFileUrl(req.getEvidenceFile());
        res.setInternName(req.getIntern().getUser().getFullName());
        res.setInternEmail(req.getIntern().getUser().getEmail());
        if (req.getHandler() != null) {
            res.setHandlerName(req.getHandler().getFullName());
        }
        return res;
    }

    //Intern hủy yêu cầu
    @Override
    public void cancelSupportRequest(Integer supportId) {
        SupportRequest supportRequest = supportRequestRepository.findById(supportId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.SUPPORT_REQUEST_NOT_EXISTS.getMessage()));
        if (supportRequest.getStatus() != SupportRequest.Status.PENDING) {
            throw new IllegalArgumentException(ErrorCode.SUPPORT_REQUEST_STATUS_INVALID.getMessage());
        }
        supportRequestRepository.delete(supportRequest);
    }

    //HR lấy tất cả yêu cầu
    @Override
    public List<GetSupportRequestResponse> getAllSupportRequest(String keyword, String status) {
        Status statusEnum = status == null ? null : SupportRequest.Status.valueOf(status);

        List<SupportRequest> supportRequests = supportRequestRepository.searchSupportRequest(keyword, statusEnum);
        return supportRequests.stream().map(supportRequest -> {
            GetSupportRequestResponse res = modelMapper.map(supportRequest, GetSupportRequestResponse.class);
            res.setEvidenceFileUrl(supportRequest.getEvidenceFile());
            res.setInternName(supportRequest.getIntern().getUser().getFullName());
            res.setInternEmail(supportRequest.getIntern().getUser().getEmail());
            if (supportRequest.getHandler() != null) {
                res.setHandlerName(supportRequest.getHandler().getFullName());
            }
            return res;
        }).toList();
    }

    //HR duyệt yêu cầu
    @Override
    public GetSupportRequestResponse approveSupportRequest(Integer supportId) {
        // K biết là có cần check hr đảm nhiệm k nx nhưng mà cứ để tạm như này :3
        SupportRequest supportRequest = supportRequestRepository.findById(supportId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.SUPPORT_REQUEST_NOT_EXISTS.getMessage()));
        // PENDING cũng duyệt được nên là làm như sau
        if (supportRequest.getStatus() == SupportRequest.Status.REJECTED) {
            throw new IllegalArgumentException(ErrorCode.SUPPORT_REQUEST_STATUS_INVALID.getMessage());
        }
        supportRequest.setStatus(SupportRequest.Status.RESOLVED);
        supportRequestRepository.save(supportRequest);
        GetSupportRequestResponse res = modelMapper.map(supportRequest, GetSupportRequestResponse.class);
        res.setEvidenceFileUrl(supportRequest.getEvidenceFile());
        res.setInternName(supportRequest.getIntern().getUser().getFullName());
        res.setInternEmail(supportRequest.getIntern().getUser().getEmail());
        if (supportRequest.getHandler() != null) {
            res.setHandlerName(supportRequest.getHandler().getFullName());
        }
        return res;
    }

    //HR nhận yêu cầu hỗ trợ
    @Override
    public GetSupportRequestResponse inProgressSupportRequest(Integer supportId) {
        SupportRequest supportRequest = supportRequestRepository.findById(supportId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.SUPPORT_REQUEST_NOT_EXISTS.getMessage()));
        if (supportRequest.getStatus() == SupportRequest.Status.PENDING) {
            throw new IllegalArgumentException(ErrorCode.SUPPORT_REQUEST_STATUS_INVALID.getMessage());
        }
        supportRequest.setStatus(SupportRequest.Status.IN_PROGRESS);
        supportRequestRepository.save(supportRequest);
        GetSupportRequestResponse res = modelMapper.map(supportRequest, GetSupportRequestResponse.class);
        res.setEvidenceFileUrl(supportRequest.getEvidenceFile());
        res.setInternName(supportRequest.getIntern().getUser().getFullName());
        res.setInternEmail(supportRequest.getIntern().getUser().getEmail());
        if (supportRequest.getHandler() != null) {
            res.setHandlerName(supportRequest.getHandler().getFullName());
        }
        return res;
    }

    //HR từ chối hỗ trợ
    @Override
    public GetSupportRequestResponse rejectSupportRequest(Integer supportId, RejectSupportRequestRequest request) {
        SupportRequest supportRequest = supportRequestRepository.findById(supportId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.SUPPORT_REQUEST_NOT_EXISTS.getMessage()));
        if (supportRequest.getStatus() == SupportRequest.Status.RESOLVED) {
            throw new IllegalArgumentException(ErrorCode.SUPPORT_REQUEST_STATUS_INVALID.getMessage());
        }
        supportRequest.setStatus(SupportRequest.Status.REJECTED);
        supportRequestRepository.save(supportRequest);
        GetSupportRequestResponse res = modelMapper.map(supportRequest, GetSupportRequestResponse.class);
        res.setEvidenceFileUrl(supportRequest.getEvidenceFile());
        res.setInternName(supportRequest.getIntern().getUser().getFullName());
        res.setInternEmail(supportRequest.getIntern().getUser().getEmail());
        if (supportRequest.getHandler() != null) {
            res.setHandlerName(supportRequest.getHandler().getFullName());
        }
        return res;
    }
}
