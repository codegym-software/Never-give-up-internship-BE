package com.example.InternShip.service;

import java.util.List;

import com.example.InternShip.dto.response.GetAllInternProgramManagerResponse;
import com.example.InternShip.dto.response.GetAllInternProgramResponse;
import com.example.InternShip.dto.response.PagedResponse;
import com.example.InternShip.dto.request.CreateInternProgramRequest;
import com.example.InternShip.dto.request.UpdateInternProgramRequest;
import org.quartz.SchedulerException;


public interface InternshipProgramService {
    public List<GetAllInternProgramResponse> getAllPrograms();
    PagedResponse<GetAllInternProgramManagerResponse> getAllInternshipPrograms(List<Integer> department, String keyword, int page);
    public void endPublish (int programId);
    public void endReviewing (int programId);
    public void startInternship(int programId);
    public GetAllInternProgramManagerResponse createInternProgram (CreateInternProgramRequest request) throws SchedulerException;
    public GetAllInternProgramManagerResponse updateInternProgram(UpdateInternProgramRequest request, int id) throws SchedulerException;
    public GetAllInternProgramManagerResponse cancelInternProgram(int id) throws SchedulerException;
    public GetAllInternProgramManagerResponse publishInternProgram(int id) throws SchedulerException;

}
