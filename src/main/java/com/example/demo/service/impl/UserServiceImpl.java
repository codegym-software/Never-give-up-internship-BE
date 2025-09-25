package com.example.demo.service.impl;

import com.example.demo.dto.request.CreateUserRequest;
import com.example.demo.dto.request.GetAllUserRequest;
import com.example.demo.dto.request.UpdateUserRequest;
import com.example.demo.dto.response.GetAllMentorResponse;
import com.example.demo.dto.response.GetAllUserResponse;
import com.example.demo.dto.response.PagedResponse;
import com.example.demo.entity.InternshipProgram;
import com.example.demo.entity.User;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public void CreateUser(CreateUserRequest request){
        String username = request.getFullName().toLowerCase().replaceAll("\\s+", "");;
        String password = "12345678";
        User user = modelMapper.map(request,User.class);
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(roleRepository.findByRoleName(request.getRole()));
    }

    @Override
    public void UpdateUser(UpdateUserRequest request){
        User user = JwtUtil.getLoggedInUser();
        modelMapper.map(request,user);
        userRepository.save(user);
    }

    @Override
    public void updateActiveStatus(Integer id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        boolean isActive = !user.isActive();
        user.setActive(isActive);
        userRepository.save(user);
    }

    @Override
    public Page<User> GetAllUser(GetAllUserRequest request, String role){
        int page = Math.max(0, request.getPage() - 1);
        PageRequest pageable = PageRequest.of(page, request.getSize());
        return userRepository.searchUsers(role, request.getKeyword(), pageable);
    }

    @Override
    public PagedResponse<GetAllUserResponse> GetAllHr(GetAllUserRequest request){
        Page<User> users= GetAllUser(request, "HR");

        PagedResponse<GetAllUserResponse> response = new PagedResponse<>();
        response.setContent(
                users.getContent().stream()
                        .map(u -> modelMapper.map(u, GetAllUserResponse.class))
                        .collect(Collectors.toList())
        );

        response.setPageNumber(users.getNumber());
        response.setPageSize(users.getSize());
        response.setTotalElements(users.getTotalElements());
        response.setTotalPages(users.getTotalPages());

        return response;
    }

    @Override
    public PagedResponse<GetAllMentorResponse> GetAllMentor(GetAllUserRequest request){
        Page<User> users= GetAllUser(request, "MENTOR");

        PagedResponse<GetAllMentorResponse> response = new PagedResponse<>();
        response.setContent(
                users.getContent().stream()
                        .map(u -> {
                            GetAllMentorResponse dto = modelMapper.map(u, GetAllMentorResponse.class);
                            dto.setExperienceYears(u.getMentor().getExperienceYears());
                            dto.setSpecialization(u.getMentor().getSpecialization());
                            dto.setGroupCount( u.getMentor().getGroup().stream()
                                    .filter(g -> g.getInternshipProgram() != null
                                            && g.getInternshipProgram().getStatus() == InternshipProgram.Status.ONGOING)
                                    .count());
                            return dto;
                        })
                        .collect(Collectors.toList())
        );

        response.setPageNumber(users.getNumber());
        response.setPageSize(users.getSize());
        response.setTotalElements(users.getTotalElements());
        response.setTotalPages(users.getTotalPages());
        return response;
    }
}
