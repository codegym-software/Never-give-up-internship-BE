package com.example.demo.service;

import com.example.demo.dto.request.CreateUserRequest;
import com.example.demo.dto.request.GetAllUserRequest;
import com.example.demo.dto.request.UpdateUserRequest;
import com.example.demo.dto.response.GetAllMentorResponse;
import com.example.demo.dto.response.GetAllUserResponse;
import com.example.demo.dto.response.PagedResponse;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;

public interface UserService {
    void CreateUser(CreateUserRequest request);
    void UpdateUser(UpdateUserRequest request);
    void updateActiveStatus(Integer id);
    Page<User> GetAllUser(GetAllUserRequest request, String role);
    PagedResponse<GetAllUserResponse> GetAllHr(GetAllUserRequest request);
    PagedResponse<GetAllMentorResponse> GetAllMentor(GetAllUserRequest request);

}
