package com.example.InternShip.controller;

import com.example.InternShip.dto.report.response.*;
import com.example.InternShip.dto.response.PagedResponse;
import com.example.InternShip.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/attendance-summary")
    public ResponseEntity<?> getAttendanceSummaryReport(
            @RequestParam(required = false) Integer teamId,
            @RequestParam(required = false) Integer internshipProgramId,
            @RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(reportService.getAttendanceSummaryReport(teamId, internshipProgramId, page));
    }

    @GetMapping("/interns/{internId}/attendance")
    public ResponseEntity<?> getInternAttendanceDetail(@PathVariable Integer internId) {
        return ResponseEntity.ok(reportService.getInternAttendanceDetail(internId));
    }

    @GetMapping("/final-report")
    public ResponseEntity<PagedResponse<FinalReportResponse>> getFinalReport(
            @RequestParam(required = false) Integer internshipProgramId,
            @RequestParam(required = false) Integer universityId,
            @RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(reportService.getFinalReport(internshipProgramId,universityId,page));
    }

    @GetMapping("/chart")
    public ResponseEntity<ChartResponse> chart(
            @RequestParam(required = false) Integer internshipProgramId,
            @RequestParam(required = false) Integer universityId) {
        return ResponseEntity.ok(reportService.chart(internshipProgramId,universityId));
    }
}