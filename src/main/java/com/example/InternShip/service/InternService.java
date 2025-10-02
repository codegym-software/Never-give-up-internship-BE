package com.example.InternShip.service;


import com.example.InternShip.dto.request.CreateInternRequest;
import com.example.InternShip.dto.response.InternResponse;
import com.example.InternShip.dto.request.GetAllInternRequest;
import com.example.InternShip.dto.response.GetInternResponse;
import com.example.InternShip.dto.response.PagedResponse;

public interface InternService {
    PagedResponse<GetInternResponse> getAllIntern (GetAllInternRequest request);
    InternResponse createIntern(CreateInternRequest request);
}

