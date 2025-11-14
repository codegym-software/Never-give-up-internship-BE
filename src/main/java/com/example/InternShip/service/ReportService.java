package com.example.InternShip.service;

import java.util.List;

import com.example.InternShip.dto.report.response.AttendanceSummaryResponse;
import com.example.InternShip.dto.report.response.InternAttendanceDetailResponse;

public interface ReportService {
    List<AttendanceSummaryResponse> getAttendanceSummaryReport(Integer teamId, Integer internshipProgramId);

    InternAttendanceDetailResponse getInternAttendanceDetail(Integer internId, Integer internshipProgramId);
}