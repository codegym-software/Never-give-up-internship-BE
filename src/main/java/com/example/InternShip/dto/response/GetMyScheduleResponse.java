package com.example.InternShip.dto.response;
import com.example.InternShip.entity.Attendance;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class GetMyScheduleResponse {
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private LocalTime checkIn;
    private LocalTime checkOut;
    private LocalDate date;
    private Attendance.Status status;
    private String team;
}
