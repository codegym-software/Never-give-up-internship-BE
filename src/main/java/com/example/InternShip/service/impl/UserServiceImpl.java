package com.example.InternShip.service.impl;

import com.example.InternShip.dto.request.CreateUserRequest;
import com.example.InternShip.dto.request.ForgetpassRequest;
import com.example.InternShip.dto.request.GetAllUserRequest;
import com.example.InternShip.dto.request.UpdateInfoRequest;
import com.example.InternShip.dto.request.UpdateUserRequest;
import com.example.InternShip.dto.response.GetUserResponse;
import com.example.InternShip.dto.response.PagedResponse;
import com.example.InternShip.entity.PendingUser;
import com.example.InternShip.entity.User;
import com.example.InternShip.entity.enums.Role;
import com.example.InternShip.exception.ErrorCode;
import com.example.InternShip.repository.PendingUserRepository;
import com.example.InternShip.repository.UserRepository;
import com.example.InternShip.service.UserService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AuthServiceImpl authService;
    private final PendingUserRepository pendingUserRepository;
    private final PendingUserServiceImpl pendingUserService;

    public PagedResponse<GetUserResponse> getAllUser(GetAllUserRequest request) {
        int page = Math.max(0, request.getPage() - 1);
        int size = 15;
        Role role = parseRole(request.getRole());
        PageRequest pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());
        Page<User> users = userRepository.searchUsers(role, request.getKeyword(), pageable);

        return new PagedResponse<>(
                users.map(user -> modelMapper.map(user, GetUserResponse.class)).getContent(),
                page + 1,
                users.getTotalElements(),
                users.getTotalPages(),
                users.hasNext(),
                users.hasPrevious());
    }

    public GetUserResponse getUserInfo() {
        User user = authService.getUserLogin();
        return modelMapper.map(user, GetUserResponse.class);
    }

    public Role parseRole(String role) {
        if (role != null && !role.isBlank()) {
            try {
                return Role.valueOf(role.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(ErrorCode.ROLE_INVALID.getMessage());
            }
        }
        return null;
    }

    public GetUserResponse creatUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException(ErrorCode.EMAIL_EXISTED.getMessage());
        }
        Role role = parseRole(request.getRole());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = modelMapper.map(request, User.class);
        user.setRole(role);
        user.setUsername(request.getEmail());
        user.setPassword(encoder.encode("12345678"));
        userRepository.save(user);
        return modelMapper.map(user, GetUserResponse.class);
    }

    public GetUserResponse updateUser(UpdateUserRequest request, int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ErrorCode.USER_NOT_EXISTED.getMessage()));
        modelMapper.map(request, user);
        user.setRole(parseRole(request.getRole()));
        userRepository.save(user);
        return modelMapper.map(user, GetUserResponse.class);
    }

    public GetUserResponse updateUserInfo(UpdateInfoRequest request) {
        User user = authService.getUserLogin();
        modelMapper.map(request, user);
        userRepository.save(user);
        return modelMapper.map(user, GetUserResponse.class);
    }

    @Override
    public void forgetPassword(ForgetpassRequest request) {
        userRepository.findByUsernameOrEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException(ErrorCode.EMAIL_INVALID.getMessage()));
        String token = UUID.randomUUID().toString();
        BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
        PendingUser pendingUser = modelMapper.map(request, PendingUser.class);
        pendingUser.setPassword(encoder.encode(request.getPassword()));
        pendingUser.setToken(token);
        pendingUser.setExpiryDate(LocalDateTime.now().plusMinutes(20));
        pendingUserRepository.save(pendingUser);
        String verifyLink = "http://localhost:8082/api/v1/pendingUser/verifyForgetPassword?token=" + token;
        pendingUserService.sendVerification(request.getEmail(), verifyLink);

    }
}
