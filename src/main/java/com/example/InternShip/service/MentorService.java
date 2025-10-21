package com.example.InternShip.service;

import com.example.InternShip.dto.request.CreateMentorRequest;
import com.example.InternShip.dto.request.UpdateMentorRequest;
import com.example.InternShip.dto.response.MentorResponse;
import java.util.List;

public interface MentorService {
    MentorResponse createMentor(CreateMentorRequest request);
    MentorResponse updateMentorDepartment(Integer mentorId, UpdateMentorRequest request);
    Object getAll(List<Integer> department, String keyword, int page);
}