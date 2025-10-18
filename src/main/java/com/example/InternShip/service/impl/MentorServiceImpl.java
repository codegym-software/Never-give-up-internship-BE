package com.example.InternShip.service.impl;

import com.example.InternShip.dto.response.GetAllMentorResponse;
import com.example.InternShip.dto.response.PagedResponse;
import com.example.InternShip.entity.Mentor;
import com.example.InternShip.entity.Team;
import com.example.InternShip.entity.User;
import com.example.InternShip.repository.InternRepository;
import com.example.InternShip.repository.MentorRepository;
import com.example.InternShip.repository.TeamRepository;
import com.example.InternShip.service.MentorService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MentorServiceImpl implements MentorService {
    private final MentorRepository mentorRepository;
    private final TeamRepository teamRepository;
    private final InternRepository internRepository;

    private final ModelMapper modelMapper;

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
