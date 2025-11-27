package com.example.InternShip.service;

import java.util.List;

import com.example.InternShip.dto.report.response.*;
import com.example.InternShip.dto.response.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportService {
    PagedResponse<AttendanceSummaryResponse> getAttendanceSummaryReport(Integer teamId, Integer internshipProgramId, int page);

    List<InternAttendanceDetailResponse> getInternAttendanceDetail(Integer internId);

    PagedResponse<FinalReportResponse> getFinalReport(Integer programId, Integer universityId, int page);

    ChartResponse chart (Integer programId, Integer universityId);
}