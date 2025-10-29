package com.example.InternShip.service;

import com.example.InternShip.dto.response.AttendanceResponse;

public interface AttendanceService {
    AttendanceResponse checkIn();
    AttendanceResponse checkOut();
}
