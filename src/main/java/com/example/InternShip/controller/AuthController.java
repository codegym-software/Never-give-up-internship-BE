package com.example.InternShip.controller;

import com.example.InternShip.dto.request.LoginRequest;
import com.example.InternShip.dto.request.RefreshTokenRequest;
import com.example.InternShip.dto.request.RegisterRequest;
import com.example.InternShip.dto.response.TokenResponse;
import com.example.InternShip.service.AuthService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/login")
    private ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest request) throws JOSEException {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    private ResponseEntity<TokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) throws JOSEException, ParseException {
        return ResponseEntity.ok(authService.refreshToken(request));
    }
}
