package com.example.demo.service;

public interface EmailService {
    void sendVerification(String email, String verifyLink);
    void verify(String token);
}
