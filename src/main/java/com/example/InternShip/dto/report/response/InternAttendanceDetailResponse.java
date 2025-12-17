package com.example.InternShip.dto.report.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class InternAttendanceDetailResponse {
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate date;
    @JsonFormat(pattern="HH:mm:ss")
    private LocalTime expectedTimeStart;
    @JsonFormat(pattern="HH:mm:ss")
    private LocalTime expectedTimeEnd;
    @JsonFormat(pattern="HH:mm:ss")
    private LocalTime actualCheckIn;
    @JsonFormat(pattern="HH:mm:ss")
    private LocalTime actualCheckOut;
    private String status;
    private List<LeaveLogEntry> leaveLogs;


    @Data
    public static class LeaveLogEntry {
        private String type;
        private String leaveStatus;
    }
}