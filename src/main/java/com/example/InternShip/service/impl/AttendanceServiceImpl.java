package com.example.InternShip.service.impl;

import com.example.InternShip.dto.response.AttendanceResponse;
import com.example.InternShip.entity.*;
import com.example.InternShip.exception.ErrorCode;
import com.example.InternShip.repository.AttendanceRepository;
import com.example.InternShip.repository.InternRepository;
import com.example.InternShip.repository.LeaveRequestRepository;
import com.example.InternShip.repository.WorkScheduleRepository;
import com.example.InternShip.service.AttendanceService;
import com.example.InternShip.service.AuthService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final InternRepository internRepository;
    private final WorkScheduleRepository workScheduleRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final AuthService authService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public AttendanceResponse checkIn() {
        User user = authService.getUserLogin();

        Intern intern = internRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.INTERN_NOT_FOUND.getMessage()));

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        //Kiểm tra xem đã check-in hôm nay chưa
        if (attendanceRepository.findByInternAndDate(intern, today).isPresent()) {
            throw new IllegalStateException(ErrorCode.ALREADY_CHECKED_IN_TODAY.getMessage());
        }
        //Lấy lịch làm việc của nhóm
        WorkSchedule schedule = getWorkScheduleForIntern(intern, today.getDayOfWeek());

        if(now.isAfter(schedule.getTimeEnd())){
            throw new IllegalStateException(ErrorCode.CANNOT_CHECKIN.getMessage());
        }

        //Tạo bản ghi chấm công mới
        Attendance newAttendance = new Attendance();
        newAttendance.setIntern(intern);
        newAttendance.setTeam(intern.getTeam());
        newAttendance.setDate(today);
        newAttendance.setCheckIn(now);
        newAttendance.setStatus(Attendance.Status.CHECKED_IN); // Trạng thái ban đầu

        //Lưu lại giờ làm việc dự kiến
        newAttendance.setTimeStart(schedule.getTimeStart());
        newAttendance.setTimeEnd(schedule.getTimeEnd());

        Attendance savedAttendance = attendanceRepository.save(newAttendance);
        return mapToResponse(savedAttendance);
    }

    @Override
    @Transactional
    public AttendanceResponse checkOut() {
        User user = authService.getUserLogin();

        Intern intern = internRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.INTERN_NOT_FOUND.getMessage()));

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        //Tìm bản ghi check-in của hôm nay
        Attendance attendance = attendanceRepository.findByInternAndDate(intern, today)
                .orElseThrow(() -> new IllegalStateException(ErrorCode.NOT_CHECKED_IN_TODAY.getMessage()));

        //Kiểm tra xem đã check-out chưa
        if (attendance.getCheckOut() != null) {
            throw new IllegalStateException(ErrorCode.ALREADY_CHECKED_OUT_TODAY.getMessage());
        }

        //Cập nhật tg check-out
        attendance.setCheckOut(now);

        //Tính toán và xét trạng thái cuối cùng
        Attendance.Status finalStatus = calculateAttendanceStatus(intern, attendance);
        attendance.setStatus(finalStatus);

        Attendance savedAttendance = attendanceRepository.save(attendance);
        return mapToResponse(savedAttendance);
    }

    //Lấy lịch làm việc của Intern (theo nhóm)
    private WorkSchedule getWorkScheduleForIntern(Intern intern, DayOfWeek day) {
        if (intern.getTeam() == null) {
            throw new IllegalStateException(ErrorCode.INTERN_NOT_TEAM.getMessage());
        }
        return workScheduleRepository.findByTeamAndDayOfWeek(intern.getTeam(), day)
                .orElseThrow(() -> new IllegalStateException(ErrorCode.SCHEDULE_NOT_SET_TODAY.getMessage()));
    }

    private Attendance.Status calculateAttendanceStatus(Intern intern, Attendance attendance) {
        LocalTime checkIn = attendance.getCheckIn();
        LocalTime checkOut = attendance.getCheckOut();

        //Lấy giờ làm việc dự kiến đã được lưu lúc check-in
        LocalTime expectedStartTime = attendance.getTimeStart();
        LocalTime expectedEndTime = attendance.getTimeEnd();

        LocalTime allowedCheckInTime = expectedStartTime.plusMinutes(30);

        //Kiểm tra đơn nghỉ phép (chỉ lấy đơn đã APPROVED)
        Optional<LeaveRequest> leaveOpt = leaveRequestRepository.findByInternAndDateAndApproved(
                intern, attendance.getDate(), true);

        if (leaveOpt.isPresent()) {
            LeaveRequest leave = leaveOpt.get();
            //Nghỉ cả ngày
            if (leave.getType() == LeaveRequest.Type.ON_LEAVE) {
                return Attendance.Status.ON_LEAVE;
            }
            //Xin đi muộn
            if (leave.getType() == LeaveRequest.Type.LATE) {
                //kiểm tra có về sớm không
                boolean isEarlyLeave = checkOut.isBefore(expectedEndTime);
                return isEarlyLeave ? Attendance.Status.EARLY_LEAVE : Attendance.Status.LATE;
            }
            //Xin về sớm
            if (leave.getType() == LeaveRequest.Type.EARLY_LEAVE) {
                //kiểm tra có đi muộn không
                boolean isLate = checkIn.isAfter(allowedCheckInTime);
                return isLate ? Attendance.Status.LATE : Attendance.Status.EARLY_LEAVE;
            }
        }

        //Nếu không có đơn, xét trạng thái dựa trên giờ
        boolean isLate = checkIn.isAfter(allowedCheckInTime);
        boolean isEarlyLeave = checkOut.isBefore(expectedEndTime);

        if (isLate && isEarlyLeave) {
            return Attendance.Status.LATE;
        } else if (isLate) {
            return Attendance.Status.LATE;
        } else if (isEarlyLeave) {
            return Attendance.Status.EARLY_LEAVE;
        } else {
            return Attendance.Status.PRESENT;
        }
    }

    // Phương thức tiện ích để map sang DTO
    private AttendanceResponse mapToResponse(Attendance attendance) {
        AttendanceResponse res = modelMapper.map(attendance, AttendanceResponse.class);
        res.setInternName(attendance.getIntern().getUser().getFullName());
        res.setTeamName(attendance.getTeam().getName());
        res.setStatus(attendance.getStatus().name());

        res.setExpectedTimeStart(attendance.getTimeStart());
        res.setExpectedTimeEnd(attendance.getTimeEnd());
        return res;
    }
}
