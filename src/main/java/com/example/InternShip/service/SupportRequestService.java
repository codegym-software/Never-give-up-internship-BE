package com.example.InternShip.service;

import java.util.List;

import com.example.InternShip.dto.supportRequest.request.CreateSupportRequestRequest;
import com.example.InternShip.dto.supportRequest.request.RejectSupportRequestRequest;
import com.example.InternShip.dto.supportRequest.request.UpdateSupportRequestRequest;
import com.example.InternShip.dto.supportRequest.response.GetSupportRequestResponse;

public interface SupportRequestService {
    //HR
    GetSupportRequestResponse createSupportRequest(CreateSupportRequestRequest request);

    List<GetSupportRequestResponse> getAllSupportRequest(String keyword, String status);

    GetSupportRequestResponse approveSupportRequest(Integer supportId);

    GetSupportRequestResponse inProgressSupportRequest(Integer supportId);

    GetSupportRequestResponse rejectSupportRequest(Integer supportId, RejectSupportRequestRequest request);

    //Intern
    GetSupportRequestResponse updateRequest(Integer id, UpdateSupportRequestRequest request);

    List<GetSupportRequestResponse> getMyList();

    void cancelSupportRequest(Integer supportId);
}
