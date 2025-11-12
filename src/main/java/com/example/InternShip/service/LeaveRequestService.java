package com.example.InternShip.service;

import com.example.InternShip.dto.request.CreateLeaveApplicationRequest;
import com.example.InternShip.dto.request.RejectLeaveApplicationRequest;
import com.example.InternShip.dto.response.*;

public interface LeaveRequestService {

    InternGetAllLeaveApplicationResponseSupport createLeaveRequest(CreateLeaveApplicationRequest request);

    PagedResponse<GetAllLeaveApplicationResponse> getAllLeaveApplication(Boolean approved, String keyword, String type,
            int page, int size);

    GetLeaveApplicationResponse viewLeaveApplication(Integer id);

    void cancelLeaveApplication(Integer id);

    void approveLeaveApplication(Integer id);

    void rejectLeaveApplication(RejectLeaveApplicationRequest request);

    InternGetAllLeaveApplicationResponse getAllLeaveApplicationByIntern(String status);
}
