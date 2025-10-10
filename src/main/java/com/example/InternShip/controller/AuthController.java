package com.example.InternShip.controller;

import com.example.InternShip.dto.request.ForgetPasswordRequest;
import com.example.InternShip.dto.request.LoginRequest;
import com.example.InternShip.dto.request.RefreshTokenRequest;
import com.example.InternShip.dto.request.RegisterRequest;
import com.example.InternShip.dto.response.TokenResponse;
import com.example.InternShip.service.AuthService;
import com.example.InternShip.service.UserService;
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
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest request) throws JOSEException {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody RefreshTokenRequest request)
            throws JOSEException, ParseException {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/forgetPassword")
    public ResponseEntity<String> forgetPassword(@RequestBody ForgetPasswordRequest request) {
            userService.forgetPassword(request);
            return ResponseEntity.ok().body("Verification code has been sent");
    }

}
