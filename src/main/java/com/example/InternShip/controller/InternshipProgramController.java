package com.example.InternShip.controller;

import com.example.InternShip.dto.request.CreateInternProgramRequest;
import com.example.InternShip.dto.request.UpdateInternProgramRequest;
import com.example.InternShip.dto.response.GetInternProgramResponse;
import com.example.InternShip.dto.response.GetAllInternProgramResponse;
import jakarta.validation.Valid;
import org.quartz.SchedulerException;

import com.example.InternShip.service.InternshipProgramService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/internship-programs")
@RequiredArgsConstructor
public class InternshipProgramController {

    private final InternshipProgramService internshipProgramService;
    @GetMapping // Cái này chắc là cho bên client
    public List<GetAllInternProgramResponse> getAllPrograms() {
        return internshipProgramService.getAllPrograms();
    }

    @GetMapping("/get") // Hàm lấy ra các chương trình thực tập (Cái này cho bên Manager)
    public ResponseEntity<?> getAllInternshipPrograms(
            @RequestParam(required = false, defaultValue = "") List<Integer> department,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "1") int page) {

        return ResponseEntity.ok(internshipProgramService.getAllInternshipPrograms(
                department,
                keyword,
                page));
    }

    @PostMapping
    public GetInternProgramResponse createInternProgram (@RequestBody @Valid CreateInternProgramRequest request) throws SchedulerException {
        return internshipProgramService.createInternProgram(request);
    }

    @PutMapping("/{id}")
    public GetInternProgramResponse updateInternProgram(@RequestBody @Valid UpdateInternProgramRequest request, @PathVariable int id) throws SchedulerException {
        return internshipProgramService.updateInternProgram(request, id);
    }

    @PatchMapping("/cancel/{id}")
    public GetInternProgramResponse cancelInternProgram(@PathVariable int id) throws SchedulerException {
        return internshipProgramService.cancelInternProgram(id);
    }

    @PatchMapping("/publish/{id}")
    public GetInternProgramResponse publishInternProgram(@PathVariable int id) throws SchedulerException {
        return internshipProgramService.publishInternProgram(id);
    }
}
