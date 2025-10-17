package com.example.InternShip.service.impl;

import com.example.InternShip.repository.MentorRepository;
import com.example.InternShip.service.MentorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MentorServiceImpl implements MentorService {
    private final MentorRepository mentorRepository;
}
