package com.example.InternShip.service.impl;

import com.example.InternShip.repository.LogRepository;
import com.example.InternShip.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {
    private final LogRepository logRepository;
}
