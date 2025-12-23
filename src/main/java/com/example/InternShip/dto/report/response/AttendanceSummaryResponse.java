package com.example.InternShip.dto.report.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AttendanceSummaryResponse {
    private Integer internId;
    private String fullName;
    private String teamName;
    private String email;

    //Tổng ngày đi làm
    private int totalWorkDays;
    //Tổng ngày có mặt
    private int totalPresentDays;
    //Tổng ngày nghỉ có phép
    private int totalOnLeaveDays;
    //Tổng ngày nghỉ không phép
    private int totalAbsentDays;
    //Tổng ngày không checkout
    private int totalCheckedInDays;
    //Tổng ngày đi/về ko đúng giờ
    private int totalTimeViolationDays;
    //Tổng ngày đi muộn/ về sớm có phép
    private int totalExcusedTimeDays;
    // tỉ lệ chuyên cần
    private BigDecimal attendancePercent;
}