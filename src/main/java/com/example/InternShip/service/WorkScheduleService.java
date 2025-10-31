package com.example.InternShip.service;

import com.example.InternShip.dto.request.SetWorkScheduleRequest;
import com.example.InternShip.dto.response.WorkScheduleResponse;
import java.time.LocalDate;


import java.time.DayOfWeek;
import java.util.List;

public interface WorkScheduleService {
    List<WorkScheduleResponse> setWorkSchedule(Integer teamId, SetWorkScheduleRequest request);

    List<WorkScheduleResponse> updateWorkSchedule(Integer teamId, SetWorkScheduleRequest request,java.time.LocalDate date);

    List<WorkScheduleResponse> getWorkSchedule(Integer teamId);

    void deleteWorkSchedule(Integer teamId, DayOfWeek dayOfWeek, java.time.LocalDate date);
}
