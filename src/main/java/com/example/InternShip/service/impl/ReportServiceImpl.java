package com.example.InternShip.service.impl;

import com.example.InternShip.dto.report.response.AttendanceSummaryResponse;
import com.example.InternShip.dto.report.response.ChartResponse;
import com.example.InternShip.dto.report.response.ChartResponse.ScoreChart;
import com.example.InternShip.dto.report.response.FinalReportResponse;
import com.example.InternShip.dto.report.response.InternAttendanceDetailResponse;
import com.example.InternShip.dto.report.response.InternAttendanceDetailResponse.LeaveLogEntry;
import com.example.InternShip.dto.response.PagedResponse;
import com.example.InternShip.entity.Attendance;
import com.example.InternShip.entity.Intern;
import com.example.InternShip.entity.LeaveRequest;
import com.example.InternShip.exception.ErrorCode;
import com.example.InternShip.repository.AttendanceRepository;
import com.example.InternShip.repository.InternRepository;
import com.example.InternShip.repository.InternshipProgramRepository;
import com.example.InternShip.repository.LeaveRequestRepository;
import com.example.InternShip.service.ReportService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final AttendanceRepository attendanceRepository;
    private final InternRepository internRepository;
    private final InternshipProgramRepository internshipProgramRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final ModelMapper modelMapper;

    @Override
    public PagedResponse<AttendanceSummaryResponse> getAttendanceSummaryReport(Integer teamId, Integer internshipProgramId, int page) {
        page = Math.max(0, page - 1);
        int size = 10;
        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<Intern> interns = internRepository.findForEvaluationReport(internshipProgramId, teamId, pageable);

        List<AttendanceSummaryResponse> responses = interns.map(intern -> {
            int totalPresentDays = totalDaysForStatus(Attendance.Status.PRESENT, intern);
            int totalOnLeaveDays = totalDaysForStatus(Attendance.Status.ON_LEAVE, intern);
            int totalAbsentDays = totalDaysForStatus(Attendance.Status.ABSENT, intern);
            int totalCheckedInDays = totalDaysForStatus(Attendance.Status.CHECKED_IN, intern);
            int totalTimeViolationDays = totalDaysForStatus(Attendance.Status.TIME_VIOLATION, intern);
            int totalExcusedTimeDays = totalDaysForStatus(Attendance.Status.EXCUSED_TIME, intern);
            int totalWorkDays = totalDaysForStatus(null, intern);
            BigDecimal attendancePercent = attendancePercent(
                    totalPresentDays, totalOnLeaveDays, totalCheckedInDays,
                    totalTimeViolationDays, totalExcusedTimeDays, totalWorkDays);

            AttendanceSummaryResponse attendanceSummaryResponse = new AttendanceSummaryResponse();
            attendanceSummaryResponse.setInternId(intern.getId());
            attendanceSummaryResponse.setFullName(intern.getUser().getFullName());
            attendanceSummaryResponse.setTeamName(intern.getTeam() != null ? intern.getTeam().getName() : null);
            attendanceSummaryResponse.setEmail(intern.getUser().getEmail());

            attendanceSummaryResponse.setTotalWorkDays(totalWorkDays);
            attendanceSummaryResponse.setTotalAbsentDays(totalAbsentDays);
            attendanceSummaryResponse.setTotalPresentDays(totalPresentDays);
            attendanceSummaryResponse.setTotalOnLeaveDays(totalOnLeaveDays);
            attendanceSummaryResponse.setTotalCheckedInDays(totalCheckedInDays);
            attendanceSummaryResponse.setTotalTimeViolationDays(totalTimeViolationDays);
            attendanceSummaryResponse.setTotalExcusedTimeDays(totalExcusedTimeDays);

            attendanceSummaryResponse.setAttendancePercent(attendancePercent);

            return attendanceSummaryResponse;
        }).toList();

        return new PagedResponse<>(
                responses,
                page + 1,
                interns.getTotalElements(),
                interns.getTotalPages(),
                interns.hasNext(),
                interns.hasPrevious());
    }

    @Override
    public List<InternAttendanceDetailResponse> getInternAttendanceDetail(Integer internId) {

        //Lấy thông tin Intern
        Intern intern = internRepository.findById(internId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.INTERN_NOT_FOUND.getMessage()));

        List<Attendance> attendances = intern.getAttendances();
        List<LeaveRequest> leaveRequests = intern.getLeaveRequests();

        //Map dữ liệu
        return attendances.stream()
                .map(attendance -> {
                    InternAttendanceDetailResponse response = new InternAttendanceDetailResponse();
                    response.setDate(attendance.getDate());
                    response.setExpectedTimeStart(attendance.getTimeStart());
                    response.setExpectedTimeEnd(attendance.getTimeEnd());
                    response.setActualCheckIn(attendance.getCheckIn());
                    response.setActualCheckOut(attendance.getCheckOut());
                    response.setStatus(attendance.getStatus().name());
                    response.setLeaveLogs(getLeaveRequestInDay(leaveRequests, attendance.getDate()));
                    return response;
                }).toList();
    }

    private List<LeaveLogEntry> getLeaveRequestInDay(List<LeaveRequest> leaveRequests, LocalDate date) {
        return leaveRequests.stream()
                .filter(leaveRequest -> leaveRequest.getDate().equals(date))
                .map(leaveRequest -> {
                    LeaveLogEntry leaveLogEntry = new LeaveLogEntry();
                    leaveLogEntry.setType(leaveRequest.getType().getMessage());
                    leaveLogEntry.setLeaveStatus(leaveRequest.getApproved() == null ? "Chờ duyệt" : leaveRequest.getApproved() ? "Đã duyệt" : "Từ chối");
                    return leaveLogEntry;
                }).toList();
    }

    private int totalDaysForStatus(Attendance.Status status, Intern intern) {
        if (intern.getAttendances() == null) return 0;

        return (int) intern.getAttendances().stream()
                .filter(a -> status == null || a.getStatus() == status)
                .count();
    }

    private BigDecimal attendancePercent(
            int totalPresentDays,
            int totalOnLeaveDays,
            int totalCheckedInDays,
            int totalTimeViolationDays,
            int totalExcusedTimeDays,
            int totalWorkDays) {
        if (totalWorkDays == 0) {
            return BigDecimal.valueOf(0);
        }

        BigDecimal presentScore = BigDecimal.valueOf(totalPresentDays);
        BigDecimal excusedTimeScore = BigDecimal.valueOf(totalExcusedTimeDays).multiply(BigDecimal.valueOf(0.75));
        BigDecimal onLeaveScore = BigDecimal.valueOf(totalOnLeaveDays).multiply(BigDecimal.valueOf(0.5));
        BigDecimal timeViolationScore = BigDecimal.valueOf(totalTimeViolationDays).multiply(BigDecimal.valueOf(0.25));
        BigDecimal checkedInScore = BigDecimal.valueOf(totalCheckedInDays).multiply(BigDecimal.valueOf(0.25));

        BigDecimal totalScore = presentScore.add(excusedTimeScore).add(onLeaveScore).add(timeViolationScore).add(checkedInScore);

        BigDecimal attendancePercent = totalScore
                .divide(BigDecimal.valueOf(totalWorkDays), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        return attendancePercent.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public PagedResponse<FinalReportResponse> getFinalReport(Integer programId, Integer universityId, int page) {
        page = Math.max(0, page - 1);
        int size = 10;
        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<Intern> interns = internRepository.findByInternshipProgramAndUniversity(programId, universityId, pageable);

        List<FinalReportResponse> responses = interns.stream().map(intern -> {
            BigDecimal attendancePercent = attendancePercent(
                    totalDaysForStatus(Attendance.Status.PRESENT, intern),
                    totalDaysForStatus(Attendance.Status.ON_LEAVE, intern),
                    totalDaysForStatus(Attendance.Status.CHECKED_IN, intern),
                    totalDaysForStatus(Attendance.Status.TIME_VIOLATION, intern),
                    totalDaysForStatus(Attendance.Status.EXCUSED_TIME, intern),
                    totalDaysForStatus(null, intern)
            );

            FinalReportResponse response = modelMapper.map(intern, FinalReportResponse.class);
            response.setAttendancePercent(attendancePercent);
            response.setAverageScore(calculateAverageScore(intern));

            return response;
        }).toList();

        return new PagedResponse<>(
                responses,
                page + 1,
                interns.getTotalElements(),
                interns.getTotalPages(),
                interns.hasNext(),
                interns.hasPrevious());
    }

    private BigDecimal calculateAverageScore(Intern intern) {
        if (intern.getQualityScore() == null ||
                intern.getExpertiseScore() == null ||
                intern.getTechnologyLearningScore() == null ||
                intern.getProblemSolvingScore() == null)
            return null;

        return intern.getQualityScore()
                .add(intern.getExpertiseScore())
                .add(intern.getTechnologyLearningScore())
                .add(intern.getProblemSolvingScore())
                .divide(BigDecimal.valueOf(4), 2, RoundingMode.HALF_UP);
    }

    @Override
    public ChartResponse chart(Integer programId, Integer universityId) {
        List<Intern> interns = internRepository
                .findByInternshipProgramAndUniversity(programId, universityId, Pageable.unpaged())
                .getContent();

        Map<Intern.Status, Integer> statusCounts = new HashMap<>();
        statusCounts.put(Intern.Status.ACTIVE, countByStatus(interns, Intern.Status.ACTIVE));
        statusCounts.put(Intern.Status.COMPLETED, countByStatus(interns, Intern.Status.COMPLETED));
        statusCounts.put(Intern.Status.DROPPED, countByStatus(interns, Intern.Status.DROPPED));
        statusCounts.put(Intern.Status.SUSPENDED, countByStatus(interns, Intern.Status.SUSPENDED));

        ScoreChart scoreChart = new ScoreChart();
        scoreChart.setScoreA(countByScore(interns, BigDecimal.valueOf(8), null));
        scoreChart.setScoreB(countByScore(interns, BigDecimal.valueOf(6.5), BigDecimal.valueOf(8)));
        scoreChart.setScoreA(countByScore(interns, null, BigDecimal.valueOf(6.5)));

        ChartResponse response = new ChartResponse();
        response.setStatusCounts(statusCounts);
        response.setScoreChart(scoreChart);

        return response;
    }

    private int countByStatus(List<Intern> interns, Intern.Status status) {
        if (interns == null) return 0;
        return (int) interns.stream().filter(intern -> intern.getStatus() == status).count();
    }

    private int countByScore(List<Intern> interns, BigDecimal minScore, BigDecimal maxScore) {
        if (interns == null) return 0;
        if (minScore == null) minScore = BigDecimal.ZERO;
        if (maxScore == null) maxScore = BigDecimal.TEN;
        BigDecimal finalMinScore = minScore;
        BigDecimal finalMaxScore = maxScore;
        return (int) interns.stream()
                .filter(intern -> {
                    BigDecimal avg = calculateAverageScore(intern);
                    return avg != null && avg.compareTo(finalMinScore) >= 0 && avg.compareTo(finalMaxScore) < 0;
                })
                .count();
    }
}