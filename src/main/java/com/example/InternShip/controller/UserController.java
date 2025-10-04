package com.example.InternShip.controller;

import com.example.InternShip.dto.request.CreateUserRequest;
import com.example.InternShip.dto.request.GetAllUserRequest;
import com.example.InternShip.dto.request.UpdateInfoRequest;
import com.example.InternShip.dto.request.UpdateUserRequest;
import com.example.InternShip.dto.response.GetUserResponse;
import com.example.InternShip.dto.response.PagedResponse;
import com.example.InternShip.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_HR')")
    @GetMapping
    public ResponseEntity<PagedResponse<GetUserResponse>> getAllUser(@ModelAttribute GetAllUserRequest request){
        return ResponseEntity.ok(userService.getAllUser(request));
    }

    @GetMapping("/info")
    public ResponseEntity<GetUserResponse> getUserInfo(){
        return ResponseEntity.ok(userService.getUserInfo());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<GetUserResponse> createUser(@RequestBody @Valid CreateUserRequest request){
        return ResponseEntity.ok(userService.creatUser(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_HR')")
    public ResponseEntity<GetUserResponse> updateUser(@RequestBody @Valid UpdateUserRequest request, @PathVariable int id){
        return ResponseEntity.ok(userService.updateUser(request,id));
    }


    @PutMapping("/info")
    public ResponseEntity<GetUserResponse> updateUserInfo(@RequestBody @Valid UpdateInfoRequest request){
        return ResponseEntity.ok(userService.updateUserInfo(request));
    }
}
