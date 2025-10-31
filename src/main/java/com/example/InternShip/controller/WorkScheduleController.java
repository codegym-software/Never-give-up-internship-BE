package com.example.InternShip.controller;

import com.example.InternShip.dto.request.SetWorkScheduleRequest;
import com.example.InternShip.service.WorkScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.DayOfWeek;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/work-schedules")
@RequiredArgsConstructor
public class WorkScheduleController {

    private final WorkScheduleService workScheduleService;

    @PostMapping("/{teamId}")
    public ResponseEntity<?> setWorkSchedule(@PathVariable Integer teamId, @RequestBody @Valid SetWorkScheduleRequest request) {
        return ResponseEntity.ok(workScheduleService.setWorkSchedule(teamId, request));
    }

    @PutMapping("/{teamId}")
    public ResponseEntity<?> updateWorkSchedule(@PathVariable Integer teamId, @RequestBody @Valid SetWorkScheduleRequest request,@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(workScheduleService.updateWorkSchedule(teamId, request,date));
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<?> getWorkSchedule(@PathVariable Integer teamId) {
        return ResponseEntity.ok(workScheduleService.getWorkSchedule(teamId));
    }

 

@DeleteMapping("/{teamId}/{dayOfWeek}")
    public ResponseEntity<?> deleteWorkSchedule(@PathVariable Integer teamId, @PathVariable DayOfWeek dayOfWeek, @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        workScheduleService.deleteWorkSchedule(teamId, dayOfWeek, date);
        return ResponseEntity.noContent().build();
    }
}

