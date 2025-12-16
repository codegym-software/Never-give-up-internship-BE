package com.example.InternShip.service.impl;

import com.example.InternShip.dto.report.response.AttendanceSummaryResponse;
import com.example.InternShip.dto.report.response.FinalAttendanceResponse;
import com.example.InternShip.dto.report.response.FinalReportResponse;
import com.example.InternShip.dto.report.response.InternAttendanceDetailResponse;
import com.example.InternShip.entity.Attendance;
import com.example.InternShip.entity.Intern;
import com.example.InternShip.entity.InternshipProgram;
import com.example.InternShip.entity.User;
import com.example.InternShip.repository.AttendanceRepository;
import com.example.InternShip.repository.InternRepository;
import com.example.InternShip.repository.InternshipProgramRepository;
import com.example.InternShip.repository.LeaveRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @Mock
    private AttendanceRepository attendanceRepository;
    @Mock
    private InternRepository internRepository;
    @Mock
    private InternshipProgramRepository internshipProgramRepository;
    @Mock
    private LeaveRequestRepository leaveRequestRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ReportServiceImpl reportService;

    private Intern intern;
    private InternshipProgram program;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setFullName("Test User");
        user.setEmail("test@example.com");

        intern = new Intern();
        intern.setId(1);
        intern.setUser(user);

        program = new InternshipProgram();
        program.setId(1);
        program.setTimeStart(LocalDateTime.now().minusDays(10));
        program.setTimeEnd(LocalDateTime.now().plusDays(10));
    }

   
}
