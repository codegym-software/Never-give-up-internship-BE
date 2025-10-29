package com.example.InternShip.service.impl;

import com.example.InternShip.dto.response.GetMyScheduleResponse;
import com.example.InternShip.dto.response.GetTeamScheduleResponse;
import com.example.InternShip.entity.*;
import com.example.InternShip.exception.ErrorCode;
import com.example.InternShip.repository.AttendanceRepository;
import com.example.InternShip.repository.InternRepository;
import com.example.InternShip.repository.TeamRepository;
import com.example.InternShip.repository.WorkScheduleRepository;
import com.example.InternShip.service.AttendanceService;
import com.example.InternShip.service.AuthService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {
    private final AuthService authService;
    private final ModelMapper modelMapper;
    private final AttendanceRepository attendanceRepository;
    private final TeamRepository teamRepository;
    private final InternRepository internRepository;
    private final WorkScheduleRepository workScheduleRepository;

    public List<GetMyScheduleResponse> getMySchedule(){
        Intern intern = authService.getUserLogin().getIntern();
        if(intern == null){
            throw new EntityNotFoundException(ErrorCode.INTERN_NOT_EXISTED.getMessage());
        }

        List<Attendance> attendances = intern.getAttendances();
        List<WorkSchedule> workSchedules = intern.getTeam().getWorkSchedules();
        AtomicReference<LocalDate> date = new AtomicReference<>(LocalDate.now());

        // map attendance vào GetMyScheduleResponse
        List<GetMyScheduleResponse> responses = attendances.stream()
                .map(attendance -> {
                    if (attendance.getDate().isEqual(LocalDate.now())){
                        date.set(LocalDate.now().plusDays(1));
                    }
                    GetMyScheduleResponse response = modelMapper.map(attendance,GetMyScheduleResponse.class);
                    response.setTeam(attendance.getTeam().getName());
                    return response;
                }).collect(Collectors.toList());

        if(intern.getInternshipProgram().getStatus() == InternshipProgram.Status.ONGOING ||
                intern.getStatus() == Intern.Status.ACTIVE) {
            // thêm dữ liệu giả vào responses
            for (int i = 0; i < 20; i++) {
                LocalDate currentDate = date.get().plusDays(i);

                // tìm ngày có thứ nằm trong workSchedules thì map và add vào responses
                workSchedules.stream()
                        .filter(ws -> ws.getDayOfWeek() == currentDate.getDayOfWeek())
                        .findFirst()
                        .ifPresent(ws -> {
                            GetMyScheduleResponse response = modelMapper.map(ws, GetMyScheduleResponse.class);
                            response.setTeam(ws.getTeam().getName());
                            response.setDate(currentDate);
                            responses.add(response);
                        });
            }
        }
        return responses;
    }

    public List<GetTeamScheduleResponse> getTeamSchedule(int teamId){
        Team team = teamRepository.findById(teamId).
                orElseThrow(() -> new EntityNotFoundException(ErrorCode.TEAM_NOT_EXISTED.getMessage()));

        List<WorkSchedule> workSchedules =team.getWorkSchedules();

        List<Attendance> attendances = attendanceRepository.findAllByTeamId(teamId);
        LocalDate date = LocalDate.now();

        Map<List<Object>, List<Attendance>> grouped = attendances.stream()
                .collect(Collectors.groupingBy(a ->
                        List.of(a.getTimeStart(), a.getTimeEnd(), a.getDate())
                ));

        List<GetTeamScheduleResponse> responses = new ArrayList<>();

        for (var entry : grouped.entrySet()) {
            List<Object> key = entry.getKey();
            List<Attendance> group = entry.getValue();

            GetTeamScheduleResponse response = new GetTeamScheduleResponse();
            response.setTimeStart((LocalTime) key.get(0));
            response.setTimeEnd((LocalTime) key.get(1));
            response.setDate((LocalDate) key.get(2));

            if (response.getDate() == date){
                date = date.plusDays(1);
            }

            // Map từng Attendance trong nhóm sang DetailTeamSchedule
            List<GetTeamScheduleResponse.DetailTeamSchedule> detailList = group.stream()
                    .map(attendance -> {
                        GetTeamScheduleResponse.DetailTeamSchedule detailTeamSchedule =
                                modelMapper.map(attendance, GetTeamScheduleResponse.DetailTeamSchedule.class);
                        detailTeamSchedule.setFullName(attendance.getIntern().getUser().getFullName());
                        detailTeamSchedule.setEmail(attendance.getIntern().getUser().getEmail());
                        return detailTeamSchedule;
                    })
                    .collect(Collectors.toList());

            response.setDetailTeamSchedules(detailList);
            responses.add(response);
        }

        if(team.getInternshipProgram().getStatus() == InternshipProgram.Status.ONGOING) {
            // thêm dữ liệu giả vào responses
            for (int i = 0; i < 20; i++) {
                LocalDate currentDate = date.plusDays(i);

                // tìm ngày có thứ nằm trong workSchedules thì map và add vào responses
                workSchedules.stream()
                        .filter(ws -> ws.getDayOfWeek() == currentDate.getDayOfWeek())
                        .findFirst()
                        .ifPresent(ws -> {
                            GetTeamScheduleResponse response = modelMapper.map(ws, GetTeamScheduleResponse.class);
                            response.setDate(currentDate);
                            responses.add(response);
                        });
            }
        }
        return responses;
    }

    @Scheduled(cron = "0 30 17 * * ?")
    public void checkAttendance() {
        LocalDate today = LocalDate.now();
        List<Intern> interns = internRepository.findAllByStatus(Intern.Status.ACTIVE);

        for (Intern intern : interns) {
            List<WorkSchedule> workSchedules = intern.getTeam().getWorkSchedules();

            if (workSchedules == null || workSchedules.isEmpty()){
                continue;
            }

            // Tìm xem hôm nay có lich ko
            WorkSchedule todaySchedule = workSchedules.stream()
                    .filter(ws -> ws.getDayOfWeek() == today.getDayOfWeek())
                    .findFirst()
                    .orElse(null);
            if (todaySchedule == null) continue;

            // Kiểm tra xem intern đã có attendance hôm nay chưa
            boolean hasAttendanceToday = intern.getAttendances().stream()
                    .anyMatch(at -> today.equals(at.getDate()));

            if (!hasAttendanceToday) {
                Attendance attendance = new Attendance();
                attendance.setDate(today);
                attendance.setIntern(intern);
                attendance.setStatus(Attendance.Status.ABSENT);
                attendance.setTimeStart(todaySchedule.getTimeStart());
                attendance.setTimeEnd(todaySchedule.getTimeEnd());
                attendanceRepository.save(attendance);
            }
        }
    }
}
