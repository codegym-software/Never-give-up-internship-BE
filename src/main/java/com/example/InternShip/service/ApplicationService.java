package com.example.InternShip.service;

import com.example.InternShip.dto.request.ApplicationRequest;
import com.example.InternShip.dto.response.ApplicationResponse;
import com.example.InternShip.dto.response.PagedResponse;
import com.example.InternShip.dto.request.SubmitApplicationContractRequest;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ApplicationService {
    ApplicationResponse submitApplication(ApplicationRequest request);
    List<ApplicationResponse> getMyApplication(); // <-- mới
   PagedResponse<ApplicationResponse> getAllApplication(Integer internshipTerm, Integer university, Integer major, String applicantName, String status, int page);
    void submitApplicationContract(SubmitApplicationContractRequest request);
}

