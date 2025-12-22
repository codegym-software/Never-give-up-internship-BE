package com.example.InternShip.controller;

import com.example.InternShip.dto.log.response.LogResponse;
import com.example.InternShip.dto.response.PagedResponse;
import com.example.InternShip.entity.Log.Model;
import com.example.InternShip.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/logs")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
public class LogController {
    private final LogService logService;

    @GetMapping
    public ResponseEntity<PagedResponse<LogResponse>> getActivityLogs(
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate toDate,
            @RequestParam(required = false) Model affected,
            @RequestParam(required = false) String searchName,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PagedResponse<LogResponse> response = logService.getActivityLogs(
                fromDate,
                toDate,
                affected,
                searchName,
                page,
                size
        );
        return ResponseEntity.ok(response);
    }
}