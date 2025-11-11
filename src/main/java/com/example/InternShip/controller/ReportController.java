package com.example.InternShip.controller;

import com.example.InternShip.dto.response.AttendanceSummaryResponse;
import com.example.InternShip.dto.response.InternAttendanceDetailResponse;
import com.example.InternShip.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/attendance-summary")
    public ResponseEntity<List<AttendanceSummaryResponse>> getAttendanceSummaryReport(
            @RequestParam(required = false) Integer teamId,
            @RequestParam(required = false) Integer internshipProgramId) {

        List<AttendanceSummaryResponse> report = reportService.getAttendanceSummaryReport(teamId, internshipProgramId);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/interns/{internId}/attendance")
    public ResponseEntity<InternAttendanceDetailResponse> getInternAttendanceDetail(
            @PathVariable Integer internId,
            @RequestParam("internshipProgramId") Integer internshipProgramId) {

        // Đã bỏ startDate và endDate
        InternAttendanceDetailResponse report = reportService.getInternAttendanceDetail(internId, internshipProgramId);
        return ResponseEntity.ok(report);
    }
}