package com.example.InternShip.service;

import com.example.InternShip.dto.request.CreateUserRequest;
import com.example.InternShip.dto.request.ForgetpassRequest;
import com.example.InternShip.dto.request.GetAllUserRequest;
import com.example.InternShip.dto.request.UpdateInfoRequest;
import com.example.InternShip.dto.request.UpdateUserRequest;
import com.example.InternShip.dto.response.GetUserResponse;
import com.example.InternShip.dto.response.PagedResponse;

public interface UserService {
    PagedResponse<GetUserResponse> getAllUser(GetAllUserRequest request);
    GetUserResponse getUserInfo();
    GetUserResponse creatUser(CreateUserRequest request);
    GetUserResponse updateUser(UpdateUserRequest request, int id);
    GetUserResponse updateUserInfo(UpdateInfoRequest request);
    void forgetPassword(ForgetpassRequest request);
}
