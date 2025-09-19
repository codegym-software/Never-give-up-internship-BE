package com.example.demo.controller;

import com.example.demo.dto.request.CreateDemoRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.service.DemoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Demo")
public class DemoController {
    @Autowired
    private DemoService demoService;

    @PostMapping("/create")
    ApiResponse<String> createDemo(@RequestBody @Valid CreateDemoRequest request){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setData(demoService.createDemo(request));
        return apiResponse;
    }
}
