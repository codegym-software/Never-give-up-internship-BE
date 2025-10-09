package com.example.InternShip.service;

import com.example.InternShip.dto.request.ApplicationRequest;
import com.example.InternShip.dto.response.ApplicationResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ApplicationService {
    ApplicationResponse submitApplication(ApplicationRequest request);
    ApplicationResponse getMyApplication(); // <-- mới

}

