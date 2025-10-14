package com.example.InternShip.service;

import com.example.InternShip.entity.InternshipApplication;

public interface EmailService {
    void sendApplicationStatusEmail(InternshipApplication application);
}