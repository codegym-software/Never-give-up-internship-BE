package com.example.InternShip.controller;

import com.example.InternShip.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/logs")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
public class LogController {
    private final LogService logService;
}
