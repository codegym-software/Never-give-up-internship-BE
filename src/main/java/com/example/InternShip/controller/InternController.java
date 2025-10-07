package com.example.InternShip.controller;

import com.example.InternShip.dto.request.UpdateInternRequest;
import com.example.InternShip.dto.response.ApiResponse;
import com.example.InternShip.exception.ErrorCode;

import com.example.InternShip.dto.request.CreateInternRequest;
import com.example.InternShip.dto.response.InternResponse;
import com.example.InternShip.entity.Intern;
import com.example.InternShip.repository.InternRepository;

import com.example.InternShip.dto.request.GetAllInternRequest;
import com.example.InternShip.dto.response.GetInternResponse;
import com.example.InternShip.dto.response.PagedResponse;

import com.example.InternShip.service.InternService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/interns")
@RequiredArgsConstructor
public class InternController {
    private final InternService internService;

    @PreAuthorize(" hasAuthority('SCOPE_HR')")
    @PutMapping("/{id}")
    public ResponseEntity<GetInternResponse> UpdateInternById(@PathVariable Integer id, @RequestBody @Valid UpdateInternRequest updateInternRequest) {
        return ResponseEntity.ok(internService.updateIntern(id, updateInternRequest));
    }


    @PostMapping
    public ResponseEntity<GetInternResponse> createIntern(@Valid @RequestBody CreateInternRequest request) {
        return ResponseEntity.ok(internService.createIntern(request));
    }

    @GetMapping
    @Operation(summary = "Get all intern", description = "show a list paginated of all intern")
    public ResponseEntity<PagedResponse<GetInternResponse>> getAllIntern (GetAllInternRequest request){
        return ResponseEntity.ok(internService.getAllIntern(request));
    }


    // @GetMapping
    //   public ResponseEntity<?> getAllIntern (
    //     @RequestParam(required = false, defaultValue = "") String keyword,
    //     @RequestParam(required = false) List<Integer> universityId,
    //     @AuthenticationPrincipal Jwt jwt,
    //     Pageable pageable

    //   ){
    //     return ResponseEntity.success(internService.getAllIntern(keyword, universityId, jwt, pageable));
    // }

}
