package com.example.InternShip.controller;
import com.example.InternShip.entity.Allowance;
import com.example.InternShip.entity.MonthlyAllowanceReport;
import com.example.InternShip.service.AllowanceReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/hr/allowance/reports") // Corrected base path
public class AllowanceReportController {

    @Autowired
    private AllowanceReportService allowanceReportService;

   

    @GetMapping("/details")
    public ResponseEntity<List<Allowance>> getAllowanceDetails(@RequestParam("month") String month) {
        try {
            YearMonth yearMonth = YearMonth.parse(month, DateTimeFormatter.ofPattern("yyyy-MM"));
            List<Allowance> allowances = allowanceReportService.getAllowanceDetailsForMonth(yearMonth);
            return ResponseEntity.ok(allowances);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<MonthlyAllowanceReport>> getGeneratedReports(Pageable pageable) {
        Page<MonthlyAllowanceReport> reports = allowanceReportService.getAllMonthlyReports(pageable);
        return ResponseEntity.ok(reports);
    }
}
