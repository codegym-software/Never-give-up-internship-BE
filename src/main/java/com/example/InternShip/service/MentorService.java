package com.example.InternShip.service;

import com.example.InternShip.dto.request.CreateMentorRequest;
import com.example.InternShip.dto.request.UpdateMentorRequest;
import com.example.InternShip.dto.response.GetAllMentorResponse;
import com.example.InternShip.dto.response.GetMentorResponse;

import java.util.List;

public interface MentorService {
    GetMentorResponse createMentor(CreateMentorRequest request);
    GetMentorResponse updateMentorDepartment(Integer mentorId, UpdateMentorRequest request);
    Object getAll(List<Integer> department, String keyword, int page);
    public List<GetAllMentorResponse> getAllMentor();
}