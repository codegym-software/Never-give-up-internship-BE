package com.example.InternShip.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.InternShip.dto.response.ApiResponse;
import com.example.InternShip.service.AdminService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('SCOPE_ADMIN') ")
public class AdminController {

    private final AdminService adminService;

    @PutMapping("/BanUser/{id}")
    public ResponseEntity<ApiResponse<String>> BanUser(@PathVariable int id) {

        adminService.BanUser(id);
        return ResponseEntity.ok().body(new ApiResponse<>(200, "ban người dùng có id " + id + " thành công", null));
    }

    @PutMapping("/UnBanUser/{id}")
    public ResponseEntity<ApiResponse<String>> UnBanUser(@PathVariable int id) {
        adminService.UnBanUser(id);
        return ResponseEntity.ok().body(new ApiResponse<>(200, "UnBan người dùng có id " + id + "thành công", null));
    }

}
