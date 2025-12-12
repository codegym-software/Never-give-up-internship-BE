package com.example.InternShip.controller;

import com.example.InternShip.dto.report.response.*;
import com.example.InternShip.dto.response.PagedResponse;
import com.example.InternShip.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PreAuthorize("hasAuthority('SCOPE_HR')")
    @GetMapping("/attendance-summary")
    public ResponseEntity<?> getAttendanceSummaryReport(
            @RequestParam(required = false) Integer teamId,
            @RequestParam(required = false) Integer internshipProgramId,
            @RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(reportService.getAttendanceSummaryReport(teamId, internshipProgramId, page));
    }

    @PreAuthorize("hasAuthority('SCOPE_HR')")
    @GetMapping("/interns/{internId}/attendance")
    public ResponseEntity<?> getInternAttendanceDetail(@PathVariable Integer internId) {
        return ResponseEntity.ok(reportService.getInternAttendanceDetail(internId));
    }

    @PreAuthorize("hasAuthority('SCOPE_HR')")
    @GetMapping("/final-report")
    public ResponseEntity<PagedResponse<FinalReportResponse>> getFinalReport(
            @RequestParam(required = false) Integer internshipProgramId,
            @RequestParam(required = false) Integer universityId,
            @RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(reportService.getFinalReport(internshipProgramId,universityId,page));
    }

    @PreAuthorize("hasAuthority('SCOPE_HR')")
    @GetMapping("/chart")
    public ResponseEntity<ChartResponse> chart(
            @RequestParam(required = false) Integer internshipProgramId,
            @RequestParam(required = false) Integer universityId) {
        return ResponseEntity.ok(reportService.chart(internshipProgramId,universityId));
    }
}