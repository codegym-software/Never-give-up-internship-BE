package com.example.InternShip.service;

import com.example.InternShip.dto.response.AttendanceResponse;
import com.example.InternShip.dto.response.GetMyScheduleResponse;
import com.example.InternShip.dto.response.GetTeamScheduleResponse;

import java.util.List;

public interface AttendanceService {
    AttendanceResponse checkIn();

    AttendanceResponse checkOut();

    List<GetMyScheduleResponse> getMySchedule();

    List<GetTeamScheduleResponse> getTeamSchedule(int teamId);

}
