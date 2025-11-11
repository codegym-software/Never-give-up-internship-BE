package com.example.InternShip.service;

import com.example.InternShip.dto.request.CreateInternRequest;
import com.example.InternShip.dto.request.UpdateInternRequest;
import com.example.InternShip.dto.request.GetAllInternRequest;
import com.example.InternShip.dto.response.GetInternResponse;
import com.example.InternShip.dto.response.MyProfileResponse;
import com.example.InternShip.dto.response.PagedResponse;

public interface InternService {
    PagedResponse<GetInternResponse> getAllIntern (GetAllInternRequest request);
    GetInternResponse createIntern(CreateInternRequest request);
    GetInternResponse updateIntern(Integer id,UpdateInternRequest updateInternRequest);
    Object getAllInternNoTeam(Integer teamId);
    MyProfileResponse getMyProfile();
    GetInternResponse getInternById(int id);
    Integer getAuthenticatedInternTeamId();
}

