package com.example.InternShip.service.impl;

import com.example.InternShip.dto.log.response.LogResponse;
import com.example.InternShip.dto.response.PagedResponse;
import com.example.InternShip.entity.Log;
import com.example.InternShip.entity.Log.Model;
import com.example.InternShip.repository.LogRepository;
import com.example.InternShip.repository.UserRepository;
import com.example.InternShip.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;
    private final UserRepository userRepository;

    @Override
    public PagedResponse<LogResponse> getActivityLogs(LocalDate fromDate, LocalDate toDate, Model affected, String searchName, int pageRaw, int size) {
        int page = Math.max(0, pageRaw - 1);
        PageRequest pageable = PageRequest.of(page, size, Sort.by("actionAt").descending());

        LocalDateTime start = (fromDate != null) ? fromDate.atStartOfDay() : null;
        LocalDateTime end = (toDate != null) ? toDate.atTime(LocalTime.MAX) : null;

        Page<Log> logs = logRepository.searchLogs(start, end, affected, searchName, pageable);

        return new PagedResponse<>(
                logs.map(this::mapToLogResponse).getContent(),
                page + 1,
                logs.getTotalElements(),
                logs.getTotalPages(),
                logs.hasNext(),
                logs.hasPrevious()
        );
    }

    private LogResponse mapToLogResponse(Log log) {
        LogResponse res = new LogResponse();
        res.setId(log.getId());
        res.setActionType(log.getActionType().name());
        res.setAffectedObject(log.getAffected().name());
        res.setActionAt(log.getActionAt());

        res.setDescription(log.getDescription());
        res.setDataOld(log.getDataOld());
        res.setDataNew(log.getDataNew());

        // Map thông tin người thực hiện
        if (log.getAction() != null) {
            res.setActionerId(log.getAction().getId());
            res.setActionerName(log.getAction().getFullName());
            res.setActionerEmail(log.getAction().getEmail());

            if(log.getAction().getRole() != null) {
                res.setActionerRole(log.getAction().getRole().name());
            }
        }
        return res;
    }
}