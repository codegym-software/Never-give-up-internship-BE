package com.example.InternShip.service;

import java.util.List;

import com.example.InternShip.dto.supportRequest.request.CreateSupportRequestRequest;
import com.example.InternShip.dto.supportRequest.request.RejectSupportRequestRequest;
import com.example.InternShip.dto.supportRequest.response.GetSupportRequestReponse;

public interface SupportRequestService {

    GetSupportRequestReponse createSupportRequest(CreateSupportRequestRequest request);

    List<GetSupportRequestReponse> getAllSupportRequest(String keyword, String status);

    GetSupportRequestReponse approveSupportRequest(Integer supportId);

    GetSupportRequestReponse inProgressSupportRequest(Integer supportId);

    GetSupportRequestReponse rejectSupportRequest(Integer supportId, RejectSupportRequestRequest request);

    void cancelSupportRequest(Integer supportId);
    
}
