package com.example.InternShip.service.impl;

import com.example.InternShip.repository.TeamRepository;
import com.example.InternShip.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
}
