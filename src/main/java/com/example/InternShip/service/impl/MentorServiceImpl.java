package com.example.InternShip.service.impl;

import com.example.InternShip.dto.request.CreateMentorRequest;
import com.example.InternShip.dto.request.UpdateMentorRequest;
import com.example.InternShip.dto.response.GetAllMentorResponse;
import com.example.InternShip.dto.response.MentorResponse;
import com.example.InternShip.dto.response.PagedResponse;
import com.example.InternShip.entity.Department;
import com.example.InternShip.entity.Mentor;
import com.example.InternShip.entity.Team;
import com.example.InternShip.entity.User;
import com.example.InternShip.entity.enums.Role;
import com.example.InternShip.exception.ErrorCode;
import com.example.InternShip.repository.*;
import com.example.InternShip.service.MentorService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MentorServiceImpl implements MentorService {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final MentorRepository mentorRepository;
    private final TeamRepository teamRepository;
    private final InternRepository internRepository;
    private final ModelMapper modelMapper;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public MentorResponse createMentor(CreateMentorRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException(ErrorCode.EMAIL_EXISTED.getMessage());
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.DEPARTMENT_NOT_EXISTED.getMessage()));

        User user = modelMapper.map(request, User.class);
        user.setUsername(request.getEmail());
        user.setPassword(passwordEncoder.encode("123456@Abc"));
        user.setRole(Role.MENTOR);
        User savedUser = userRepository.save(user);

        Mentor mentor = new Mentor();
        mentor.setUser(savedUser);
        mentor.setDepartment(department);
        Mentor savedMentor = mentorRepository.save(mentor);

        MentorResponse mentorResponse = modelMapper.map(savedUser, MentorResponse.class);
        mentorResponse.setMentorId(savedMentor.getId());
        mentorResponse.setDepartmentName(department.getName());

        return mentorResponse;
    }

    @Override
    @Transactional
    public MentorResponse updateMentorDepartment(Integer mentorId, UpdateMentorRequest request) {

        Mentor mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MENTOR_NOT_EXISTED.getMessage()));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.DEPARTMENT_NOT_EXISTED.getMessage()));

        mentor.setDepartment(department);
        Mentor savedMentor = mentorRepository.save(mentor);

        MentorResponse response = modelMapper.map(savedMentor, MentorResponse.class);
        response.setMentorId(savedMentor.getId());

        return response;
    }

    @Override
    public PagedResponse<GetAllMentorResponse> getAll(List<Integer> department, String keyword, int page) {
        page = Math.min(0, page - 1);
        PageRequest pageable = PageRequest.of(page, 10);

        // Kiểm tra null vì Hibernate không coi List rỗng là null
        if (department == null || department.isEmpty()) {
            department = null;
        }

        Page<Mentor> mentors = mentorRepository.searchMentor(department, keyword, pageable);

        List<GetAllMentorResponse> responses = mentors.stream()
                .map(mentor -> {
                    User user = mentor.getUser();
                    GetAllMentorResponse res = modelMapper.map(user, GetAllMentorResponse.class);
                    Team team = teamRepository.findByMentor_id(mentor.getId());
                    // Lấy ra id nhóm dựa trên id mentor rồi đếm tất cả intern có id nhóm đó
                    int totalInternInGroup = internRepository.countByTeam_id(team.getId());
                    res.setTotalInternOwn(totalInternInGroup);
                    res.setDepartmentName(mentor.getDepartment().getName());
                    return res;
                })
                .collect(Collectors.toList());

        return new PagedResponse<>(
                responses,
                page + 1,
                mentors.getTotalElements(),
                mentors.getTotalPages(),
                mentors.hasNext(),
                mentors.hasPrevious());
    }
}