package com.example.InternShip.service;

import com.example.InternShip.dto.request.CreateUserRequest;
import com.example.InternShip.dto.request.ForgetPasswordRequest;
import com.example.InternShip.dto.request.GetAllUserRequest;
import com.example.InternShip.dto.request.UpdateInfoRequest;
import com.example.InternShip.dto.request.UpdateUserRequest;
import com.example.InternShip.dto.response.GetUserResponse;
import com.example.InternShip.dto.response.PagedResponse;
import com.example.InternShip.dto.request.ChangeMyPasswordRequest;

public interface UserService {
    PagedResponse<GetUserResponse> getAllUser(GetAllUserRequest request);
    GetUserResponse getUserInfo();
    GetUserResponse createUser(CreateUserRequest request);
    GetUserResponse updateUser(UpdateUserRequest request, int id);
    GetUserResponse updateUserInfo(UpdateInfoRequest request);
    void forgetPassword(ForgetPasswordRequest request);
    void changePassword(ChangeMyPasswordRequest request);

}
