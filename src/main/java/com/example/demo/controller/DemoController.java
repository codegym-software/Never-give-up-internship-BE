package com.example.demo.controller;

import com.example.demo.dto.request.CreateDemoRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.service.DemoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {
    @Autowired
    private DemoService demoService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('demo:create')")
    ApiResponse<String> createDemo(@RequestBody @Valid CreateDemoRequest request){
        return new ApiResponse<String>(demoService.createDemo(request));
    }
}
