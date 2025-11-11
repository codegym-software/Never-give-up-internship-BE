package com.example.InternShip.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class InternAttendanceDetailResponse {

    private Integer internId;
    private String fullName;
    private String email;
    private String teamName;

    private List<DailyLogEntry> dailyLogs;

    private List<LeaveLogEntry> leaveLogs;

    @Data
    public static class LeaveLogEntry {
        private java.time.LocalDate date;
        private String type;
        private String reason;
        private String leaveStatus;
        private String approverName;
    }

    @Data
    public static class DailyLogEntry {
        private java.time.LocalDate date;
        private java.time.LocalTime expectedTimeStart;
        private java.time.LocalTime expectedTimeEnd;
        private java.time.LocalTime actualCheckIn;
        private java.time.LocalTime actualCheckOut;
        private String status;
    }
}