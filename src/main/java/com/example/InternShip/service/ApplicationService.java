package com.example.InternShip.service;

import com.example.InternShip.dto.request.ApplicationRequest;
import com.example.InternShip.dto.request.ApproveApplicationRequest;
import com.example.InternShip.dto.request.UpdateApplicationStatusRequest;
import com.example.InternShip.dto.response.ApplicationResponse;
import com.example.InternShip.dto.response.PagedResponse;
import com.example.InternShip.dto.request.SubmitApplicationContractRequest;

public interface ApplicationService {
    ApplicationResponse submitApplication(ApplicationRequest request);

    ApplicationResponse getMyApplication(); // <-- mới

    PagedResponse<ApplicationResponse> getAllApplication(Integer internshipTerm, Integer university, Integer major, String applicantName, String status, int page);

    void submitApplicationContract(SubmitApplicationContractRequest request);

    void approveApplication(ApproveApplicationRequest request);

    ApplicationResponse updateApplicationStatus(Integer applicationId, UpdateApplicationStatusRequest request);
}

