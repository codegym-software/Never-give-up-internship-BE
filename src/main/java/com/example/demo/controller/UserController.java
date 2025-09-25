package com.example.demo.controller;

import com.example.demo.dto.request.CreateUserRequest;
import com.example.demo.dto.request.GetAllUserRequest;
import com.example.demo.dto.request.UpdateUserRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.GetAllMentorResponse;
import com.example.demo.dto.response.GetAllUserResponse;
import com.example.demo.dto.response.PagedResponse;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public ApiResponse CreateUser(@RequestBody @Valid CreateUserRequest request){
        userService.CreateUser(request);
        return new ApiResponse();
    }

    @PutMapping("/edit")
    public ApiResponse EditUser(@RequestBody @Valid UpdateUserRequest request){
        userService.UpdateUser(request);
        return new ApiResponse();
    }

    @PatchMapping("/updateActiveStatus/id")
    public ApiResponse updateActiveStatus(@PathVariable Integer id){
        userService.updateActiveStatus(id);
        return new ApiResponse();
    }

    @GetMapping("/GetAllHr")
    public ApiResponse<PagedResponse<GetAllUserResponse>> GetAllHr(GetAllUserRequest request){
        return new ApiResponse(userService.GetAllHr(request));
    }

    @GetMapping("/GetAllMentor")
    public ApiResponse<PagedResponse<GetAllMentorResponse>> GetAllMentor(GetAllUserRequest request){
        return new ApiResponse(userService.GetAllMentor(request));
    }
}
