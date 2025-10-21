package com.example.InternShip.controller;

import com.example.InternShip.dto.request.CreateInternProgramRequest;
import com.example.InternShip.dto.request.UpdateInternProgramRequest;
import com.example.InternShip.dto.response.GetAllInternProgramManagerResponse;
import com.example.InternShip.dto.response.GetAllInternProgramResponse;
import jakarta.validation.Valid;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.*;

import com.example.InternShip.service.InternshipProgramService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/v1/internship-programs")
@RequiredArgsConstructor
public class InternshipProgramController {

    private final InternshipProgramService internshipProgramService;
    @GetMapping
    public List<GetAllInternProgramResponse> getAllPrograms() {
        return internshipProgramService.getAllPrograms();
    }

    @PostMapping
    public GetAllInternProgramManagerResponse createInternProgram (@RequestBody @Valid CreateInternProgramRequest request) throws SchedulerException {
        return internshipProgramService.createInternProgram(request);
    }

    @PutMapping("/{id}")
    public GetAllInternProgramManagerResponse updateInternProgram(@RequestBody @Valid UpdateInternProgramRequest request, @PathVariable int id) throws SchedulerException {
        return internshipProgramService.updateInternProgram(request, id);
    }

    @PatchMapping("/cancel/{id}")
    public GetAllInternProgramManagerResponse cancelInternProgram(@PathVariable int id) throws SchedulerException {
        return internshipProgramService.cancelInternProgram(id);
    }

    @PatchMapping("/publish/{id}")
    public GetAllInternProgramManagerResponse publishInternProgram(@PathVariable int id) throws SchedulerException {
        return internshipProgramService.publishInternProgram(id);
    }
}
