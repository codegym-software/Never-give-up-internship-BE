package com.example.InternShip.service;

import com.example.InternShip.dto.response.AttendanceSummaryResponse;
import com.example.InternShip.dto.response.InternAttendanceDetailResponse;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    List<AttendanceSummaryResponse> getAttendanceSummaryReport(Integer teamId, Integer internshipProgramId);

    InternAttendanceDetailResponse getInternAttendanceDetail(Integer internId, Integer internshipProgramId);
}