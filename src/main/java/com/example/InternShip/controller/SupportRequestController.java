package com.example.InternShip.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.InternShip.dto.supportRequest.request.CreateSupportRequestRequest;
import com.example.InternShip.dto.supportRequest.request.RejectSupportRequestRequest;
import com.example.InternShip.service.SupportRequestService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/suport-request")
@RequiredArgsConstructor
public class SupportRequestController {
    private final SupportRequestService supportRequestService;

    // Tạo đơn hỗ trợ
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> createSupportRequest(@ModelAttribute @Valid CreateSupportRequestRequest request) {
        return ResponseEntity.ok(supportRequestService.createSupportRequest(request));
    }

    // Lấy toàn bộ đơn hỗ trợ
    @GetMapping
    public ResponseEntity<?> getAllSupportRequest(
            @RequestParam(required = false, defaultValue = "") String keyword, // Tên + Email
            @RequestParam(required = false, defaultValue = "") String status) {
        return ResponseEntity.ok(supportRequestService.getAllSupportRequest(keyword, status));
    }

    // Duyệt đơn hỗ trợ
    @PutMapping("/approve/{supportId}")
    public ResponseEntity<?> approveSupportRequest(@PathVariable Integer supportId) {
        return ResponseEntity.ok(supportRequestService.approveSupportRequest(supportId));
    }

    // Chuyển trạng thái đơn hỗ trợ thành đang xử lý
    @PutMapping("/inProgress/{supportId}")
    public ResponseEntity<?> inProgressSupportRequest(@PathVariable Integer supportId) {
        return ResponseEntity.ok(supportRequestService.inProgressSupportRequest(supportId));
    }

    // Từ chối đơn hỗ trợ thành đang xử lý
    @PutMapping("/reject/{supportId}")
    public ResponseEntity<?> rejectSupportRequest(@PathVariable Integer supportId,
            @RequestBody RejectSupportRequestRequest request) {
        return ResponseEntity.ok(supportRequestService.rejectSupportRequest(supportId, request));
    }

    // Hủy đơn hỗ trợ thành đang xử lý
    @DeleteMapping("/cancel/{supportId}")
    public ResponseEntity<String> cancelSupportRequest(@PathVariable Integer supportId) {
        supportRequestService.cancelSupportRequest(supportId);
        return ResponseEntity.ok("OK");
    }
}
