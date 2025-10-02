package com.example.InternShip.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.InternShip.dto.response.ApiResponse;
import com.example.InternShip.service.AdminService;
import com.example.InternShip.service.impl.AdminServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<ApiResponse> BanUser(@PathVariable int id) {
        // TODO: process PUT request

        adminService.BanUser(id);
        ApiResponse adApiResponse = new ApiResponse<>(200, "ban người dùng có id " + id + " thành công", null);
        return ResponseEntity.ok().body(adApiResponse);
    }

    @PutMapping("/UnBanUser/{id}")
    public ResponseEntity<ApiResponse> UnBanUser(@PathVariable int id) {
        // TODO: process PUT request
        adminService.UnBanUser(id);
        ApiResponse adApiResponse = new ApiResponse<>(200, "UnBan người dùng có id " + id + "thành công", null);
        return ResponseEntity.ok().body(adApiResponse);
    }

}
