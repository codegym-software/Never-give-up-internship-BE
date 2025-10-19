package com.example.InternShip.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.InternShip.dto.response.GetAllInternProgramResponse;
import com.example.InternShip.entity.Intern;
import com.example.InternShip.entity.InternshipApplication;
import com.example.InternShip.entity.User;
import com.example.InternShip.entity.enums.Role;
import com.example.InternShip.exception.ErrorCode;
import com.example.InternShip.repository.InternRepository;
import com.example.InternShip.repository.InternshipApplicationRepository;
import com.example.InternShip.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.quartz.*;
import org.springframework.stereotype.Service;

import com.example.InternShip.entity.InternshipProgram;
import com.example.InternShip.repository.InternshipProgramRepository;
import com.example.InternShip.service.InternshipProgramService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InternshipProgramServiceImpl implements InternshipProgramService {
    private final InternshipProgramRepository internshipProgramRepository;
    private final InternshipApplicationRepository internshipApplicationRepository;
    private final InternRepository internRepository;
    private final UserRepository userRepository;
    private final AuthServiceImpl authService;
    private final ModelMapper modelMapper;
    private final Scheduler scheduler;

    @Override
    public List<GetAllInternProgramResponse> getAllPrograms() {
        Role role = authService.getUserLogin().getRole();
        List<InternshipProgram> results = null;
        if (role.equals(Role.VISITOR)) {
            results = internshipProgramRepository.findAllByStatus(InternshipProgram.Status.PUBLISHED);
        }else {
            results = internshipProgramRepository.findAll();
        }
        return results.stream()
                .map(result -> modelMapper.map(result, GetAllInternProgramResponse.class))
                .toList();
    }

    public void endPublish (int programId){
        InternshipProgram internshipProgram = internshipProgramRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException(ErrorCode.INTERNSHIP_TERM_NOT_EXISTED.getMessage()));
        List<InternshipApplication> applications = internshipProgram.getApplications();
        List<InternshipApplication> toUpdate = new ArrayList<>();

        for (InternshipApplication app : applications){
            if (app.getStatus() == InternshipApplication.Status.SUBMITTED){
                app.setStatus(InternshipApplication.Status.UNDER_REVIEW);
                toUpdate.add(app);
            }
        }
        internshipProgram.setStatus(InternshipProgram.Status.REVIEWING);
        internshipProgramRepository.save(internshipProgram);
        internshipApplicationRepository.saveAll(toUpdate);
    }

    public void endReviewing (int programId){
        InternshipProgram internshipProgram = internshipProgramRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException(ErrorCode.INTERNSHIP_TERM_NOT_EXISTED.getMessage()));
        List<InternshipApplication> applications = internshipProgram.getApplications();
        List<InternshipApplication> toUpdate = new ArrayList<>();

        for (InternshipApplication app : applications){
            if (app.getStatus() == InternshipApplication.Status.UNDER_REVIEW){
                app.setStatus(InternshipApplication.Status.REJECTED);
                toUpdate.add(app);
            }
        }
        internshipProgram.setStatus(InternshipProgram.Status.PENDING);
        internshipProgramRepository.save(internshipProgram);
        internshipApplicationRepository.saveAll(toUpdate);
    }

    public void startInternship(int programId){
        InternshipProgram internshipProgram = internshipProgramRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException(ErrorCode.INTERNSHIP_TERM_NOT_EXISTED.getMessage()));
        List<InternshipApplication> applications = internshipProgram.getApplications();
        List<Intern> toCreate = new ArrayList<>();
        List<User> toUpdate = new ArrayList<>();
        List<InternshipApplication> toUpdateApp = new ArrayList<>();

        for (InternshipApplication app : applications){
            if (app.getStatus() == InternshipApplication.Status.CONFIRM){
                Intern intern = modelMapper.map(app,Intern.class);
                intern.setStatus(Intern.Status.ACTIVE);
                toCreate.add(intern);

                User user = app.getUser();
                user.setRole(Role.INTERN);
                toUpdate.add(user);
            } else if (app.getStatus() == InternshipApplication.Status.APPROVED) {
                app.setStatus(InternshipApplication.Status.NOT_CONTRACT);
                toUpdateApp.add(app);
            }
        }
        internshipProgram.setStatus(InternshipProgram.Status.ONGOING);
        internshipProgramRepository.save(internshipProgram);
        internRepository.saveAll(toCreate);
        userRepository.saveAll(toUpdate);
        internshipApplicationRepository.saveAll(toUpdateApp);
    }

    public void scheduleInternship (int programId, Class<? extends Job> jobClass, LocalDate timeStart) throws SchedulerException {
        Date startDate = Date.from(timeStart.atStartOfDay(ZoneId.systemDefault()).toInstant());

        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity("job_" + programId, jobClass.getSimpleName())
                .usingJobData("programId", programId)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger_" + programId, jobClass.getSimpleName())
                .startAt(startDate)
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }
}
