package com.example.InternShip.controller;

import com.example.InternShip.service.InternService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/intern")
@RequiredArgsConstructor
public class InternController {
    private final InternService internService;
}
