package com.example.InternShip.service;

import com.example.InternShip.dto.request.CreateInternRequest;
import com.example.InternShip.dto.response.InternResponse;

public interface InternService {
    InternResponse createIntern(CreateInternRequest request);
}