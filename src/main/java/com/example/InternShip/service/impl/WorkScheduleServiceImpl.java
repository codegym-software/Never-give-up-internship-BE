package com.example.InternShip.service.impl;

import com.example.InternShip.dto.request.SetWorkScheduleRequest;
import com.example.InternShip.dto.response.WorkScheduleResponse;
import com.example.InternShip.entity.Team;
import com.example.InternShip.entity.WorkSchedule;
import com.example.InternShip.exception.ErrorCode;
import com.example.InternShip.repository.TeamRepository;
import com.example.InternShip.repository.WorkScheduleRepository;
import com.example.InternShip.service.WorkScheduleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class WorkScheduleServiceImpl implements WorkScheduleService {

    private final WorkScheduleRepository workScheduleRepository;
    private final TeamRepository teamRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public List<WorkScheduleResponse> setWorkSchedule(Integer teamId, SetWorkScheduleRequest request) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.TEAM_NOT_EXISTED.getMessage()));

        List<WorkSchedule> newSchedules = new ArrayList<>();
        for (SetWorkScheduleRequest.WorkScheduleItem item : request.getSchedules()) {
            // Check if a schedule for this day already exists
            workScheduleRepository.findByTeamAndDayOfWeek(team, item.getDayOfWeek()).ifPresent(s -> {
                throw new IllegalStateException("Lịch làm việc cho ngày " + item.getDayOfWeek() + " đã tồn tại.");
            });

            WorkSchedule schedule = new WorkSchedule();
            schedule.setTeam(team);
            schedule.setDayOfWeek(item.getDayOfWeek());
            schedule.setTimeStart(item.getTimeStart());
            schedule.setTimeEnd(item.getTimeEnd());
            newSchedules.add(schedule);
        }

        List<WorkSchedule> savedSchedules = workScheduleRepository.saveAll(newSchedules);

        return savedSchedules.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<WorkScheduleResponse> updateWorkSchedule(Integer teamId, SetWorkScheduleRequest request,LocalDate date) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.TEAM_NOT_EXISTED.getMessage()));
 if (date.isBefore(LocalDate.now())) {
            throw new IllegalStateException("Không thể chỉnh sửa lịch làm việc của một ngày trong quá khứ.");
        }
if (date.equals(LocalDate.now())) {
    throw new IllegalStateException("Không thể chỉnh sửa lịch làm việc của ngày hiện tại ");
}

        List<WorkSchedule> schedulesToSave = new ArrayList<>();

        for (SetWorkScheduleRequest.WorkScheduleItem item : request.getSchedules()) {
            Optional<WorkSchedule> existingScheduleOpt = workScheduleRepository.findByTeamAndDayOfWeek(team, item.getDayOfWeek());

            WorkSchedule scheduleToSave;
            if (existingScheduleOpt.isPresent()) {
                // Update existing schedule
                scheduleToSave = existingScheduleOpt.get();
                scheduleToSave.setTimeStart(item.getTimeStart());
                scheduleToSave.setTimeEnd(item.getTimeEnd());
            } else {
                // Create new schedule
                scheduleToSave = new WorkSchedule();
                scheduleToSave.setTeam(team);
                scheduleToSave.setDayOfWeek(item.getDayOfWeek());
                scheduleToSave.setTimeStart(item.getTimeStart());
                scheduleToSave.setTimeEnd(item.getTimeEnd());
            }
            schedulesToSave.add(scheduleToSave);
        }

        List<WorkSchedule> savedSchedules = workScheduleRepository.saveAll(schedulesToSave);

        return savedSchedules.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteWorkSchedule(Integer teamId, DayOfWeek dayOfWeek, LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalStateException("Không thể xóa lịch làm việc của một ngày trong quá khứ.");
        }
        if (date.equals(LocalDate.now())) {
            
            throw new IllegalStateException("Không thể xóa lịch làm việc của ngày hiện tại");
        }

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.TEAM_NOT_EXISTED.getMessage()));

        WorkSchedule scheduleToDelete = workScheduleRepository.findByTeamAndDayOfWeek(team, dayOfWeek)
                .orElseThrow(() -> new EntityNotFoundException("Lịch làm việc cho ngày " + dayOfWeek + " của nhóm " + team.getName() + " không tồn tại."));

        workScheduleRepository.delete(scheduleToDelete);
    }

    @Override
    public List<WorkScheduleResponse> getWorkSchedule(Integer teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.TEAM_NOT_EXISTED.getMessage()));

        return team.getWorkSchedules().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private WorkScheduleResponse mapToResponse(WorkSchedule workSchedule) {
        WorkScheduleResponse response = modelMapper.map(workSchedule, WorkScheduleResponse.class);
        response.setTeamName(workSchedule.getTeam().getName());
        return response;
    }
}
